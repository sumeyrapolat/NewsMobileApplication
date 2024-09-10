package com.example.newsmobileapplication.di.module

import android.content.SharedPreferences
import com.example.newsmobileapplication.data.preferences.FavoriteManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoriteModule {

    // Provides a singleton instance of FavoriteManager
    @Provides
    @Singleton
    fun provideFavoriteManager(sharedPreferences: SharedPreferences): FavoriteManager {
        return FavoriteManager(sharedPreferences)
    }
}
