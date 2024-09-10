package com.example.newsmobileapplication.model.repository

import android.net.Uri
import android.util.Log
import com.example.newsmobileapplication.model.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage // Firebase Storage reference added
) {
    private var cachedUserData: User? = null

    // Signs in the user with email and password
    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Loads user data from Firestore using the userID
    suspend fun loadUserData(userID: String): Result<User> {
        return try {
            val document = db.collection("Users").document(userID).get().await()

            if (document.exists()) {
                val userData = document.toObject(User::class.java)

                userData?.let {
                    cachedUserData = it  // Cache the data
                    Result.success(it)
                } ?: Result.failure(Exception("User data is null"))
            } else {
                Result.failure(Exception("User data not found"))
            }
        } catch (e: FirebaseFirestoreException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Returns cached user data if available
    fun getCachedUserData(): User? {
        return cachedUserData
    }

    // Sends a password reset email to the specified email address
    fun sendPasswordResetEmail(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onError(task.exception?.message ?: "Failed to send password reset email")
            }
        }
    }

    // Signs out the currently logged-in user
    fun signOut() {
        auth.signOut()
    }

    // Checks if a user is currently logged in
    fun checkUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Returns the ID of the currently logged-in user, or null if no user is logged in
    fun getCurrentUserID(): String? {
        val currentUser = auth.currentUser
        return currentUser?.uid
    }

    // Uploads the profile photo to Firebase Storage and updates the user's profile photo URL in Firestore
    suspend fun uploadProfilePhoto(userID: String, imageUri: Uri): Result<String> {
        return try {
            val storageRef: StorageReference = storage.reference.child("profilePhotos/$userID.jpg")
            val uploadTask = storageRef.putFile(imageUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()

            // Save the photo URL to Firestore
            db.collection("Users").document(userID).update("profilePhotoUrl", downloadUrl).await()

            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Returns the cached profile photo URL if available
    fun getProfilePhotoUrl(): String? {
        return cachedUserData?.profilePhotoUrl
    }

    // Loads the current user's data based on their user ID
    suspend fun loadCurrentUserData(): Result<User> {
        val userId = getCurrentUserID()
        return if (userId != null) {
            loadUserData(userId)
        } else {
            Result.failure(Exception("No user logged in"))
        }
    }
}
