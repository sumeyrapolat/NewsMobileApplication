package com.example.newsmobileapplication.data.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

    fun saveLoginData(email: String, rememberMe: Boolean) {
        sharedPreferences.edit().apply {
            putString("email", email)
            putBoolean("rememberMe", rememberMe)
            apply()
        }
    }

    fun getEmail(): String? {
        return sharedPreferences.getString("email", "")
    }

    fun isRememberMeChecked(): Boolean {
        return sharedPreferences.getBoolean("rememberMe", false)
    }

    fun clearLoginData() {
        sharedPreferences.edit().apply {
            remove("email")
            putBoolean("rememberMe", false)
            apply()
        }
    }
}