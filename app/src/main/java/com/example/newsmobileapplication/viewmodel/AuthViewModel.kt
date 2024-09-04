package com.example.newsmobileapplication.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmobileapplication.model.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _passwordResetState = MutableStateFlow<PasswordResetState>(PasswordResetState.Idle)
    val passwordResetState: StateFlow<PasswordResetState> = _passwordResetState

    private val _userDataState = MutableStateFlow<UserDataState>(UserDataState.Loading)
    val userDataState: StateFlow<UserDataState> = _userDataState

    private val _userLoggedInState = MutableStateFlow<Boolean>(false)
    val userLoggedInState: StateFlow<Boolean> = _userLoggedInState

    private val _profilePhotoState = MutableStateFlow<ProfilePhotoState>(ProfilePhotoState.Idle)
    val profilePhotoState: StateFlow<ProfilePhotoState> = _profilePhotoState

    private val _userProfilePhotoUri = MutableStateFlow<Uri?>(null)
    val userProfilePhotoUri: StateFlow<Uri?> = _userProfilePhotoUri

    init {
        checkUserLoggedIn()
        // Eğer kullanıcı giriş yapmışsa, önbellekteki verilerin yüklü olup olmadığını kontrol ediyoruz.
        if (_userLoggedInState.value) {
            loadCachedUserDataIfNeeded()
        }
    }

    private fun checkUserLoggedIn() {
        _userLoggedInState.value = authRepository.checkUserLoggedIn()
    }

    fun onViewInitialized() {
        if (_userDataState.value !is UserDataState.Success) {
            loadUserData()
        }
    }

    fun loadCachedUserDataIfNeeded() {
        val cachedData = authRepository.getCachedUserData()
        if (cachedData != null) {
            _userDataState.value = UserDataState.Success(
                firstName = cachedData.firstName,
                lastName = cachedData.lastName,
                email = cachedData.email
            )
            _userProfilePhotoUri.value = authRepository.getProfilePhotoUrl()?.let { Uri.parse(it) }
            Log.d("AuthViewModel", "Cached data loaded: $cachedData")
        } else {
            loadUserData()
        }
    }

    fun loadUserData() {
        val userID = authRepository.getCurrentUserID()
        if (userID != null) {
            viewModelScope.launch {
                val result = authRepository.loadUserData(userID)
                if (result.isSuccess) {
                    val userData = result.getOrNull()
                    Log.d("AuthViewModel", "LoadUserData result: $userData")
                    if (userData != null) {
                        _userDataState.value = UserDataState.Success(
                            firstName = userData.firstName,
                            lastName = userData.lastName,
                            email = userData.email
                        )
                        _userProfilePhotoUri.value = authRepository.getProfilePhotoUrl()?.let { Uri.parse(it) }
                    } else {
                        Log.d("AuthViewModel", "User data is null.")
                        _userDataState.value = UserDataState.Error("User data is null")
                    }
                } else {
                    val errorMessage = when (result.exceptionOrNull()) {
                        is FirebaseAuthInvalidCredentialsException -> "Invalid credentials"
                        is FirebaseAuthInvalidUserException -> "User not found"
                        else -> "Unknown error"
                    }
                    Log.d("AuthViewModel", "Error loading user data: $errorMessage")
                    _userDataState.value = UserDataState.Error(errorMessage)
                }
            }
        } else {
            Log.d("AuthViewModel", "User not logged in.")
            _userDataState.value = UserDataState.Error("User not logged in")
        }
    }

    fun sendPasswordResetEmail(email: String) {
        _passwordResetState.value = PasswordResetState.Loading
        authRepository.sendPasswordResetEmail(
            email = email,
            onSuccess = {
                _passwordResetState.value = PasswordResetState.Success("Password reset email sent")
                Log.d("AuthViewModel", "Password reset email sent successfully.")
            },
            onError = { errorMessage ->
                _passwordResetState.value = PasswordResetState.Error(errorMessage)
                Log.e("AuthViewModel", "Error sending password reset email: $errorMessage")
            }
        )
    }

    fun signOut() {
        authRepository.signOut()
        _userDataState.value = UserDataState.Error("User not logged in")
        _userLoggedInState.value = false
        Log.d("AuthViewModel", "User signed out")
    }


    fun resetPasswordResetState() {
        _passwordResetState.value = PasswordResetState.Idle
    }

    fun forgotPassword(email: String) {
        sendPasswordResetEmail(email)
    }

    fun updateUserProfilePhoto(imageUri: Uri) {
        val userID = authRepository.getCurrentUserID()
        if (userID != null) {
            viewModelScope.launch {
                _profilePhotoState.value = ProfilePhotoState.Loading
                val result = authRepository.uploadProfilePhoto(userID, imageUri)
                if (result.isSuccess) {
                    _profilePhotoState.value = ProfilePhotoState.Success(result.getOrNull()!!)
                    _userProfilePhotoUri.value = imageUri // URI'yi güncelle
                } else {
                    _profilePhotoState.value = ProfilePhotoState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            }
        } else {
            Log.e("AuthViewModel", "No user is currently logged in.")
            _profilePhotoState.value = ProfilePhotoState.Error("No user is currently logged in.")
        }
    }

    sealed class ProfilePhotoState {
        object Idle : ProfilePhotoState()
        object Loading : ProfilePhotoState()
        data class Success(val photoUrl: String) : ProfilePhotoState()
        data class Error(val message: String) : ProfilePhotoState()
    }

    sealed class PasswordResetState {
        object Idle : PasswordResetState()
        object Loading : PasswordResetState()
        data class Success(val message: String) : PasswordResetState()
        data class Error(val message: String) : PasswordResetState()
    }

    sealed class UserDataState {
        object Loading : UserDataState()
        data class Success(val firstName: String, val lastName: String, val email: String) : UserDataState()
        data class Error(val message: String) : UserDataState()
    }
}
