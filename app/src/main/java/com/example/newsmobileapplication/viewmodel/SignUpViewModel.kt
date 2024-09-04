package com.example.newsmobileapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmobileapplication.model.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState = _signUpState.asStateFlow()

    fun signUp(email: String, password: String, firstName: String, lastName: String) {
        _signUpState.value = SignUpState.Loading
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userID = auth.currentUser?.uid
                        val userEmail = auth.currentUser?.email
                        Log.d("SignUpViewModel", "User ID: $userID")
                        Log.d("SignUpViewModel", "User Email: $userEmail")
                        Log.d("SignUpViewModel", "First Name: $firstName, Last Name: $lastName")

                        if (userID != null) {
                            saveUserToFirestore(userID, firstName, lastName, userEmail ?: email)
                        } else {
                            _signUpState.value = SignUpState.Error("User email not found")
                            Log.e("SignUpViewModel", "User email not found")
                        }
                    } else {
                        _signUpState.value = SignUpState.Error(task.exception?.message ?: "Unknown error")
                        Log.e("SignUpViewModel", "Error: ${task.exception?.message ?: "Unknown error"}")
                    }
                }
        }
    }


    private fun saveUserToFirestore(userID: String, firstName: String, lastName: String, email: String) {
        val signUpUser = User(firstName = firstName, lastName = lastName, email = email)
        Log.d("SignUpViewModel", "Saving User: $signUpUser with ID: $userID")

        db.collection("Users").document(userID).set(signUpUser)
            .addOnSuccessListener {
                Log.d("SignUpViewModel", "User saved to Firestore successfully")
                _signUpState.value = SignUpState.Success("Sign up successful!")
            }
            .addOnFailureListener { e ->
                _signUpState.value = SignUpState.Error("Failed to save user to Firestore: ${e.message}")
                Log.e("SignUpViewModel", "Failed to save user to Firestore: ${e.message}")
            }
    }

    fun resetSignUpState() {
        _signUpState.value = SignUpState.Idle
    }
}


// Kayıt durumu için durum sınıfları
sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    data class Success(val message: String) : SignUpState()
    data class Error(val error: String) : SignUpState()
}

