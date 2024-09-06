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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: FeedRepository,
    private val favoriteManager: FavoriteManager
) : ViewModel() {

    private val _newsItems = MutableStateFlow<List<NewsItem>?>(null)
    val newsItems: StateFlow<List<NewsItem>?> = _newsItems

    init {
        fetchTopStories("technology")  // Varsayılan olarak teknoloji haberleri çekiyoruz
    }

    fun toggleFavorite(newsId: String) {
        if (favoriteManager.isFavorite(newsId)) {
            favoriteManager.removeFavorite(newsId)
        } else {
            favoriteManager.addFavorite(newsId)
        }
    }

    fun isFavorite(newsId: String): Boolean {
        return favoriteManager.isFavorite(newsId)
    }

    // Haberleri çekmek için getTopStories çağırıyoruz
    private fun fetchTopStories(section: String) {
        viewModelScope.launch {
            try {
                val news = repository.getNews(section)?.map { newsItem ->
                    // Her haber için URL'ye göre ID oluşturuyoruz
                    val generatedId = generateNewsItemId(newsItem.url)
                    Log.d("FeedViewModel", "Generated ID for news item '${newsItem.title}': $generatedId")
                    newsItem.copy(id = generatedId)
                }

                // Haberleri filtreleyip _newsItems StateFlow'u güncelliyoruz
                _newsItems.value = news?.filter { isLatinAlphabet(it.title) }

                if (_newsItems.value.isNullOrEmpty()) {
                    Log.d("FeedViewModel", "No news available")
                } else {
                    Log.d("FeedViewModel", "News items fetched successfully")
                }

            } catch (e: Exception) {
                Log.e("FeedViewModel", "Error fetching news: ${e.message}", e)
            }
        }
    }

    fun isLatinAlphabet(text: String): Boolean {
        return text.all { it in '\u0000'..'\u007F' }
    }

    // URL'ye göre haber çekiyoruz
    fun getNewsItemById(newsItemId: String): NewsItem? {
        val newsItem = newsItems.value?.find { it.id == newsItemId }
        Log.d("FeedViewModel", "Fetching newsItem with id: $newsItemId, found: $newsItem")
        return newsItem
    }

    fun getFavoriteNewsItems(): List<NewsItem> {
        val favoriteIds = favoriteManager.getFavorites()
        return newsItems.value?.filter { it.id in favoriteIds } ?: emptyList()
    }

    fun removeFavorite(newsId: String) {
        favoriteManager.removeFavorite(newsId)
    }
}
