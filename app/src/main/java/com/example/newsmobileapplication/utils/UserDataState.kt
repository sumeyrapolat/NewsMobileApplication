package com.example.newsmobileapplication.utils

sealed class UserDataState {
    object Loading : UserDataState()
    data class Success(val firstName: String, val lastName: String, val email: String) : UserDataState()
    data class Error(val message: String) : UserDataState()
}