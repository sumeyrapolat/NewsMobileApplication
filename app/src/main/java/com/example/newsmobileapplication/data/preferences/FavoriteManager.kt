package com.example.newsmobileapplication.data.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    private val FAVORITES_KEY = "favorites" // Key for storing favorites in SharedPreferences

    // Adds a news item to the list of favorites
    fun addFavorite(newsId: String) {
        val favorites = getFavorites().toMutableSet()
        favorites.add(newsId)
        sharedPreferences.edit().putStringSet(FAVORITES_KEY, favorites).apply()
    }

    // Removes a news item from the list of favorites
    fun removeFavorite(newsId: String) {
        val favorites = getFavorites().toMutableSet()
        favorites.remove(newsId)
        sharedPreferences.edit().putStringSet(FAVORITES_KEY, favorites).apply()
    }

    // Checks if a news item is in the list of favorites
    fun isFavorite(newsId: String): Boolean {
        return getFavorites().contains(newsId)
    }

    // Retrieves the list of favorite news items
    fun getFavorites(): Set<String> {
        return sharedPreferences.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
    }
}
