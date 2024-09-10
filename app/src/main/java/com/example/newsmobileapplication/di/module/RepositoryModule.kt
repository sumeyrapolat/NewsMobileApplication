package com.example.newsmobileapplication.di.module

import com.example.newsmobileapplication.data.api.NewsApiService
import com.example.newsmobileapplication.model.repository.FeedRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // Provides a singleton instance of FeedRepository
    @Provides
    @Singleton
    fun provideFeedRepository(
        apiService: NewsApiService
    ): FeedRepository {
        return FeedRepository(apiService)
    }
}

