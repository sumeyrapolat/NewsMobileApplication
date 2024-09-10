package com.example.newsmobileapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmobileapplication.data.preferences.FavoriteManager
import com.example.newsmobileapplication.model.entities.Article
import com.example.newsmobileapplication.model.entities.NewsItem
import com.example.newsmobileapplication.model.repository.FeedRepository
import com.example.newsmobileapplication.utils.ApiResult
import com.example.newsmobileapplication.utils.generateNewsItemId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: FeedRepository,
) : ViewModel() {

    private val _newsItems = MutableStateFlow<ApiResult<List<NewsItem>>>(ApiResult.Loading)
    val newsItems: StateFlow<ApiResult<List<NewsItem>>> = _newsItems

    private val _favoriteNewsItems = MutableStateFlow<List<NewsItem>>(emptyList())
    val favoriteNewsItems: StateFlow<List<NewsItem>> = _favoriteNewsItems

    private val _newsDetail = MutableStateFlow<ApiResult<NewsItem>>(ApiResult.Loading)
    val newsDetail: StateFlow<ApiResult<NewsItem>> = _newsDetail

    private val _searchResults = MutableStateFlow<ApiResult<List<Article>>>(ApiResult.Loading)
    val searchResults: StateFlow<ApiResult<List<Article>>> = _searchResults

    init {
        fetchNewsByCategory("World")
    }

    fun fetchNewsByCategory(section: String) {
        viewModelScope.launch {
            _newsItems.value = ApiResult.Loading

            try {
                val result = repository.getNews(section)
                _newsItems.value = result
            } catch (e: Exception) {
                _newsItems.value = ApiResult.Error(e)
                Log.e("CategoryViewModel", "Error fetching news for category $section: ${e.message}")
            }
        }
    }

    fun fetchArticlesByQuery(query: String) {
        viewModelScope.launch {
            _searchResults.value = ApiResult.Loading

            val result = repository.searchArticles(query)

            if (result is ApiResult.Success) {
                val articles = result.data.map { article ->
                    val generatedID = generateNewsItemId(article.webUrl)
                    article.copy(id = generatedID)
                }
                _searchResults.value = ApiResult.Success(articles)
            } else if (result is ApiResult.Error) {
                _searchResults.value = ApiResult.Error(result.exception)
            }
        }
    }
}







