package com.example.newsmobileapplication.data.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    private val FAVORITES_KEY = "favorites"

    fun addFavorite(newsId: String) {
        val favorites = getFavorites().toMutableSet()
        favorites.add(newsId)
        sharedPreferences.edit().putStringSet(FAVORITES_KEY, favorites).apply()
    }

    fun removeFavorite(newsId: String) {
        val favorites = getFavorites().toMutableSet()
        favorites.remove(newsId)
        sharedPreferences.edit().putStringSet(FAVORITES_KEY, favorites).apply()
    }

    fun isFavorite(newsId: String): Boolean {
        return getFavorites().contains(newsId)
    }

    fun getFavorites(): Set<String> {
        return sharedPreferences.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
    }
}
