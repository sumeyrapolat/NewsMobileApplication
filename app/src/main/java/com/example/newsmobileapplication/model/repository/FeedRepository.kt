package com.example.newsmobileapplication.model.repository

import com.example.newsmobileapplication.data.api.NewsApiService
import com.example.newsmobileapplication.model.entities.Article
import com.example.newsmobileapplication.model.entities.NewsItem
import com.example.newsmobileapplication.utils.ApiResult
import com.example.newsmobileapplication.utils.generateNewsItemId
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val apiService: NewsApiService
) {

    // Fetches top news stories for the specified section
    suspend fun getNews(section: String): ApiResult<List<NewsItem>> {
        return try {
            val response = apiService.getTopStories(section)
            if (response.isSuccessful) {
                val newsList = response.body()?.results?.map { newsItem ->
                    newsItem.copy(id = generateNewsItemId(newsItem.url)) // Generate an ID from the URL
                } ?: emptyList()
                ApiResult.Success(newsList)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                ApiResult.Error(Exception("Error fetching top stories: $errorMessage"))
            }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

    // Searches articles based on the provided query string
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
