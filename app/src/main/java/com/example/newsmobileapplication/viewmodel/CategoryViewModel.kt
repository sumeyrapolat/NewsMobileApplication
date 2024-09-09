package com.example.newsmobileapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmobileapplication.data.preferences.FavoriteManager
import com.example.newsmobileapplication.model.entities.Article
import com.example.newsmobileapplication.model.entities.NewsItem
import com.example.newsmobileapplication.model.repository.FeedRepository
import com.example.newsmobileapplication.utils.ApiResult
import com.example.newsmobileapplication.utils.generateArticleId
import com.example.newsmobileapplication.utils.generateNewsItemId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: FeedRepository,
    private val favoriteManager: FavoriteManager
) : ViewModel() {

    // Use ApiResult to handle different states (Loading, Success, Error)
    private val _newsItems = MutableStateFlow<ApiResult<List<NewsItem>>>(ApiResult.Loading)
    val newsItems: StateFlow<ApiResult<List<NewsItem>>> = _newsItems

    private val _searchResults = MutableStateFlow<ApiResult<List<Article>>>(ApiResult.Loading)
    val searchResults: StateFlow<ApiResult<List<Article>>> = _searchResults

    private val _favoriteArticles = MutableStateFlow<List<String>>(emptyList())
    val favoriteArticles: StateFlow<List<String>> = _favoriteArticles

    // Fetch news by category using ApiResult
    fun fetchNewsByCategory(section: String) {
        viewModelScope.launch {
            _newsItems.value = ApiResult.Loading  // Set loading state initially

            val result = repository.getNews(section)

            if (result is ApiResult.Success) {
                // Apply map to the success data
                val news = result.data.map { newsItem ->
                    val generatedId = generateNewsItemId(newsItem.url)
                    newsItem.copy(id = generatedId)
                }
                Log.d("CategoryViewModel", "Fetched news: $news")
                _newsItems.value = ApiResult.Success(news) // Pass the modified list
            } else if (result is ApiResult.Error) {
                Log.e("CategoryViewModel", "Error fetching news: ${result.exception.message}")
                _newsItems.value = ApiResult.Error(result.exception)  // Propagate the error
            }
        }
    }

    // Fetch articles by query using ApiResult
    fun fetchArticlesByQuery(query: String) {
        viewModelScope.launch {
            _searchResults.value = ApiResult.Loading  // Set loading state initially

            val result = repository.searchArticles(query)

            if (result is ApiResult.Success) {
                // Apply map to the success data
                val articles = result.data.map { article ->
                    val generatedID = generateArticleId(article.webUrl)
                    article.copy(id = generatedID)
                }
                Log.d("CategoryViewModel", "Fetched articles: $articles")
                _searchResults.value = ApiResult.Success(articles) // Pass the modified list
            } else if (result is ApiResult.Error) {
                Log.e("CategoryViewModel", "Error fetching articles: ${result.exception.message}")
                _searchResults.value = ApiResult.Error(result.exception)  // Propagate the error
            }
        }
    }

    // Get a specific article by ID from search results
    fun getSearchArticleById(articleId: String): Article? {
        val result = (_searchResults.value as? ApiResult.Success)?.data?.find { it.id == articleId }
        Log.d("CategoryViewModel", "Fetched search article: $result")
        return result
    }

    // Toggle favorite status for an article
    fun toggleFavorite(articleId: String) {
        viewModelScope.launch {
            if (_favoriteArticles.value.contains(articleId)) {
                _favoriteArticles.value -= articleId
                favoriteManager.removeFavorite(articleId)
                Log.d("CategoryViewModel", "Removed from favorites: $articleId")
            } else {
                _favoriteArticles.value += articleId
                favoriteManager.addFavorite(articleId)
                Log.d("CategoryViewModel", "Added to favorites: $articleId")
            }
        }
    }

    // Clear search results
    fun clearSearchResults() {
        _searchResults.value = ApiResult.Success(emptyList())
    }

    // Check if an article is a favorite
    fun isFavorite(articleId: String): Boolean {
        return _favoriteArticles.value.contains(articleId)
    }
}
