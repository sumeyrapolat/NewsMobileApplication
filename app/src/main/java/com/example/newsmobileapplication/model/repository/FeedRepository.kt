package com.example.newsmobileapplication.model.repository

import android.util.Log
import com.example.newsmobileapplication.data.api.NewsApiService
import com.example.newsmobileapplication.model.entities.NewsItem
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val apiService: NewsApiService
) {
    suspend fun getNews(section: String): List<NewsItem>? {
        val response = apiService.getTopStories(section = section)
        if (response.isSuccessful) {
            Log.d("FeedRepository", "API Response: ${response.body()?.results}")
            return response.body()?.results?.take(50) // İlk 10 haberi alıyoruz
        } else {
            Log.e("FeedRepository", "API call failed: ${response.errorBody()?.string()}")
            return null
        }
    }
}
