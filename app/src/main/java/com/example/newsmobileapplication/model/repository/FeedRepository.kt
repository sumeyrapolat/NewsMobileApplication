package com.example.newsmobileapplication.model.repository

import com.example.newsmobileapplication.data.api.NewsApiService
import com.example.newsmobileapplication.model.entities.NewsItem
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val apiService: NewsApiService
) {
    suspend fun getNews(from: String): List<NewsItem>? {
        val response = apiService.getNews(from = from)
        return if (response.isSuccessful) {
            response.body()?.articles
        } else {
            null
        }
    }
}
