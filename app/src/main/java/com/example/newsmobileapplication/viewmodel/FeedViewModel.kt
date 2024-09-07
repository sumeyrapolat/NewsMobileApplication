package com.example.newsmobileapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmobileapplication.data.preferences.FavoriteManager
import com.example.newsmobileapplication.model.entities.NewsItem
import com.example.newsmobileapplication.model.repository.FeedRepository
import com.example.newsmobileapplication.utils.generateNewsItemId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: FeedRepository,
    private val favoriteManager: FavoriteManager
) : ViewModel() {

    private val _newsItems = MutableStateFlow<List<NewsItem>?>(null)
    val newsItems: StateFlow<List<NewsItem>?> = _newsItems

    private val _favoriteNewsItems = MutableStateFlow<List<NewsItem>>(emptyList())
    val favoriteNewsItems: StateFlow<List<NewsItem>> = _favoriteNewsItems

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchTopStories("home")  // Varsayılan olarak teknoloji haberleri çekiyoruz
        loadFavorites()  // Favori haberleri başlatma sırasında yükleyelim
    }

    // Favori ekleme/çıkarma işlemi
    fun toggleFavorite(newsId: String) {
        viewModelScope.launch {
            if (favoriteManager.isFavorite(newsId)) {
                favoriteManager.removeFavorite(newsId)
            } else {
                favoriteManager.addFavorite(newsId)
            }
            // Favori haberlerin güncellenmesi
            loadFavorites()
        }
    }

    // Favori haberleri yükleme
    private fun loadFavorites() {
        viewModelScope.launch {
            val favoriteIds = favoriteManager.getFavorites()
            _favoriteNewsItems.value = _newsItems.value?.filter { it.id in favoriteIds } ?: emptyList()
        }
    }

    fun isFavorite(newsId: String): Boolean {
        return favoriteManager.isFavorite(newsId)
    }

    // Haberleri çekmek için getTopStories çağırıyoruz
    fun fetchTopStories(section: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Reset error message before each fetch

            try {
                val news = repository.getNews(section)?.map { newsItem ->
                    val generatedId = generateNewsItemId(newsItem.url)
                    newsItem.copy(id = generatedId)
                }
                _newsItems.value = news
                loadFavorites()  // Favorilerdeki haberlerin güncellenmesi
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching news: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // URL'ye göre haber çekiyoruz
    fun getNewsItemById(newsItemId: String): NewsItem? {
        val newsItem = newsItems.value?.find { it.id == newsItemId }
        Log.d("FeedViewModel", "Fetching newsItem with id: $newsItemId, found: $newsItem")
        return newsItem
    }

    // Favori haberleri alma
    fun getFavoriteNewsItems(): List<NewsItem> {
        return _favoriteNewsItems.value
    }

    fun removeFavorite(newsId: String) {
        viewModelScope.launch {
            favoriteManager.removeFavorite(newsId)
            loadFavorites()  // Favoriler güncelleniyor
        }
    }
}
