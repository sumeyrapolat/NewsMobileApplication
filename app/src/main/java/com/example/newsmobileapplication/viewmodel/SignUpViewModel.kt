package com.example.newsmobileapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmobileapplication.model.entities.User
import com.example.newsmobileapplication.utils.SignUpState
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
                        if (userID != null) {
                            saveUserToFirestore(userID, firstName, lastName, userEmail ?: email)
                        } else {
                            _signUpState.value = SignUpState.Error("User email not found")
                        }
                    } else {
                        _signUpState.value = SignUpState.Error(task.exception?.message ?: "Unknown error")
                    }
                }
        }
    }

    private fun saveUserToFirestore(userID: String, firstName: String, lastName: String, email: String) {
        val signUpUser = User(firstName = firstName, lastName = lastName, email = email)

        db.collection("Users").document(userID).set(signUpUser)
            .addOnSuccessListener {
                _signUpState.value = SignUpState.Success("Sign up successful!")
            }
            .addOnFailureListener { e ->
                _signUpState.value = SignUpState.Error("Failed to save user to Firestore: ${e.message}")
            }
    }

    fun resetSignUpState() {
        _signUpState.value = SignUpState.Idle
    }
}

