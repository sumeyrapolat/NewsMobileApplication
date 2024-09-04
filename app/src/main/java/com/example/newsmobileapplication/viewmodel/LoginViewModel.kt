package com.example.newsmobileapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmobileapplication.model.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun signIn(email: String, password: String, onSuccess: () -> Unit) {
        Log.d("SignInViewModel", "signIn called with email: $email")
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val result = authRepository.signIn(email, password)
            if (result.isSuccess) {
                Log.d("SignInViewModel", "signIn successful")
                onSuccess()
                authRepository.loadUserData(auth.currentUser!!.uid    )
                _loginState.value = LoginState.Success("Sign in successful!")
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                Log.d("SignInViewModel", "signIn failed with error: $errorMessage")
                _loginState.value = LoginState.Error(errorMessage)
            }
        }
    }

    fun resetSignInState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val message: String) : LoginState()
    data class Error(val error: String) : LoginState()
}
