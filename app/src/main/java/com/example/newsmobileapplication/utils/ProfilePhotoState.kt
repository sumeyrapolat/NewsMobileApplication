package com.example.newsmobileapplication.utils

sealed class ProfilePhotoState {
    object Idle : ProfilePhotoState()
    object Loading : ProfilePhotoState()
    data class Success(val photoUrl: String) : ProfilePhotoState()
    data class Error(val message: String) : ProfilePhotoState()
}

