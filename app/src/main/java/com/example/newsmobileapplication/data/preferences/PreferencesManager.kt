package com.example.newsmobileapplication.data.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE) // SharedPreferences for storing login data

    // Saves login data (email and remember me option) to SharedPreferences
    fun saveLoginData(email: String, rememberMe: Boolean) {
        sharedPreferences.edit().apply {
            putString("email", email)
            putBoolean("rememberMe", rememberMe)
            apply()
        }
    }

    // Retrieves the saved email from SharedPreferences
    fun getEmail(): String? {
        return sharedPreferences.getString("email", "")
    }

    // Checks if the remember me option was selected
    fun isRememberMeChecked(): Boolean {
        return sharedPreferences.getBoolean("rememberMe", false)
    }

    // Clears saved login data from SharedPreferences
    fun clearLoginData() {
        sharedPreferences.edit().apply {
            remove("email")
            putBoolean("rememberMe", false)
            apply()
        }
    }
}
