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
    private val favoriteManager: FavoriteManager
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
        fetchNewsByCategory("home") // Varsayılan olarak "home" kategorisini çekiyoruz
        loadFavorites()  // Favorileri yükleme
    }

    // Kategoriye göre haberleri çekme
    fun fetchNewsByCategory(section: String) {
        viewModelScope.launch {
            _newsItems.value = ApiResult.Loading  // İlk başta yükleme durumu

            try {
                val result = repository.getNews(section)
                _newsItems.value = result  // Sonucu (Success/Error) set ediyoruz
                loadFavorites()  // Favorileri yükledikten sonra
            } catch (e: Exception) {
                _newsItems.value = ApiResult.Error(e)
                Log.e("CategoryViewModel", "Error fetching news for category $section: ${e.message}")
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            val favoriteIds = favoriteManager.getFavorites()
            Log.d("CategoryViewModel", "Loaded favorite IDs: $favoriteIds")

            val newsItemsResult = _newsItems.value
            if (newsItemsResult is ApiResult.Success) {
                val favoriteNews = newsItemsResult.data.filter { it.id in favoriteIds }
                _favoriteNewsItems.value = favoriteNews
                Log.d("CategoryViewModel", "Favorite news: $favoriteNews")
            } else {
                _favoriteNewsItems.value = emptyList()
            }
        }
    }

    // Haber ID'ye göre favori olup olmadığını kontrol etme
    fun isFavorite(newsId: String): Boolean {
        return favoriteManager.isFavorite(newsId)
    }

    // Haber ID ile haber detayı çekme
    fun fetchNewsItemById(newsItemId: String) {
        viewModelScope.launch {
            _newsDetail.value = ApiResult.Loading
            try {
                val newsItemsResult = _newsItems.value
                if (newsItemsResult is ApiResult.Success) {
                    val newsItem = newsItemsResult.data.find { it.id == newsItemId }
                    if (newsItem != null) {
                        _newsDetail.value = ApiResult.Success(newsItem)
                        Log.d("CategoryViewModel", "Fetched News Item: ${newsItem.id}")
                    } else {
                        _newsDetail.value = ApiResult.Error(Exception("News item not found for ID: $newsItemId"))
                    }
                } else {
                    _newsDetail.value = ApiResult.Error(Exception("News items are not yet loaded"))
                }
            } catch (e: Exception) {
                _newsDetail.value = ApiResult.Error(e)
                Log.e("CategoryViewModel", "Error fetching news item: ${e.message}")
            }
        }
    }

    // Favori ekleme veya çıkarma
    fun toggleFavorite(newsId: String) {
        viewModelScope.launch {
            if (favoriteManager.isFavorite(newsId)) {
                favoriteManager.removeFavorite(newsId)
            } else {
                favoriteManager.addFavorite(newsId)
            }
            loadFavorites()  // Favoriler güncellendikten sonra tekrar yükleme
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
                    val generatedID = generateNewsItemId(article.webUrl)
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

}







