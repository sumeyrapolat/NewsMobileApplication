package com.example.newsmobileapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmobileapplication.data.preferences.FavoriteManager
import com.example.newsmobileapplication.model.entities.Article
import com.example.newsmobileapplication.model.entities.NewsItem
import com.example.newsmobileapplication.model.repository.AuthRepository
import com.example.newsmobileapplication.model.repository.FeedRepository
import com.example.newsmobileapplication.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: FeedRepository,
    private val favoriteManager: FavoriteManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _newsItems = MutableStateFlow<ApiResult<List<NewsItem>>>(ApiResult.Loading)
    val newsItems: StateFlow<ApiResult<List<NewsItem>>> = _newsItems

    private val _favoriteNewsItems = MutableStateFlow<List<NewsItem>>(emptyList())
    val favoriteNewsItems: StateFlow<List<NewsItem>> = _favoriteNewsItems

    private val _searchResults = MutableStateFlow<ApiResult<List<Article>>>(ApiResult.Loading)
    val searchResults: StateFlow<ApiResult<List<Article>>> = _searchResults

    private val _newsDetail = MutableStateFlow<ApiResult<NewsItem>>(ApiResult.Loading)
    val newsDetail: StateFlow<ApiResult<NewsItem>> = _newsDetail

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName

    init {
        viewModelScope.launch {
            val result = authRepository.loadCurrentUserData()
            if (result.isSuccess) {
                _userName.value = result.getOrNull()?.firstName
            } else {
                _userName.value = "Unknown"
            }
        }
    }
    init {
        fetchTopStories("home")
        loadFavorites()
    }

    fun toggleFavorite(newsId: String) {
        viewModelScope.launch {
            if (favoriteManager.isFavorite(newsId)) {
                favoriteManager.removeFavorite(newsId)
            } else {
                favoriteManager.addFavorite(newsId)
            }
            val newsItemsResult = _newsItems.value
            if (newsItemsResult is ApiResult.Success) {
                loadFavorites()
            }
        }
    }

    fun fetchNewsByCategory(section: String) {
        fetchTopStories(section)
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            val favoriteIds = favoriteManager.getFavorites()
            Log.d("ViewModel", "Loaded favorite IDs: $favoriteIds")

            val newsItemsResult = _newsItems.value
            if (newsItemsResult is ApiResult.Success) {
                val favoriteNews = newsItemsResult.data.filter { it.id in favoriteIds }
                _favoriteNewsItems.value = favoriteNews
                Log.d("ViewModel", "Favorite news: $favoriteNews")
            } else {
                _favoriteNewsItems.value = emptyList()
            }
        }
    }

    fun isFavorite(newsId: String): Boolean {
        return favoriteManager.isFavorite(newsId)
    }

    fun fetchTopStories(section: String) {
        viewModelScope.launch {
            _newsItems.value = ApiResult.Loading

            try {
                val result = repository.getNews(section)
                _newsItems.value = result
                loadFavorites()
            } catch (e: Exception) {
                _newsItems.value = ApiResult.Error(e)
                Log.e("FeedViewModel", "Error fetching top stories: ${e.message}")
            }
        }
    }


    fun fetchNewsItemById(newsItemId: String) {
        viewModelScope.launch {
            _newsDetail.value = ApiResult.Loading
            try {
                val newsItemsResult = _newsItems.value
                if (newsItemsResult is ApiResult.Success) {
                    val newsItem = newsItemsResult.data.find { it.id == newsItemId }
                    if (newsItem != null) {
                        _newsDetail.value = ApiResult.Success(newsItem)
                        Log.d("FeedViewModel", "Fetched News Item: ${newsItem.id}")
                    } else {
                        _newsDetail.value = ApiResult.Error(Exception("News item not found for ID: $newsItemId"))
                        Log.e("FeedViewModel", "News item not found for ID: $newsItemId")
                    }
                } else {
                    _newsDetail.value = ApiResult.Error(Exception("News items are not yet loaded"))
                    Log.e("FeedViewModel", "News items are not yet loaded")
                }
            } catch (e: Exception) {
                _newsDetail.value = ApiResult.Error(e)
                Log.e("FeedViewModel", "Error fetching news item: ${e.message}")
            }
        }
    }

    fun removeFavorite(newsId: String) {
        viewModelScope.launch {
            favoriteManager.removeFavorite(newsId)
            loadFavorites()
        }
    }
}
