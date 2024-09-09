package com.example.newsmobileapplication.model.repository

import com.example.newsmobileapplication.data.api.NewsApiService
import com.example.newsmobileapplication.model.entities.Article
import com.example.newsmobileapplication.model.entities.NewsItem
import com.example.newsmobileapplication.utils.ApiResult
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val apiService: NewsApiService
) {

    suspend fun getNews(section: String): ApiResult<List<NewsItem>> {
        return try {
            val response = apiService.getTopStories(section = section)
            if (response.isSuccessful) {
                // Safely unwrap the response body
                val newsList = response.body()?.results?.take(20) ?: emptyList()
                ApiResult.Success(newsList)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                ApiResult.Error(Exception("Error fetching top stories: $errorMessage"))
            }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

    suspend fun searchArticles(query: String): ApiResult<List<Article>> {
        return try {
            val response = apiService.searchArticles(query = query)
            if (response.isSuccessful) {
                // Safely unwrap the response body
                val articlesList = response.body()?.response?.docs ?: emptyList()
                ApiResult.Success(articlesList)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                ApiResult.Error(Exception("Error searching articles: $errorMessage"))
            }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
}

