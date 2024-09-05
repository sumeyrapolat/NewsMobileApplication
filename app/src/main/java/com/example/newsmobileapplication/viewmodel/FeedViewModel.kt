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
        fetchNews()
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

    private fun fetchNews() {
        viewModelScope.launch {
            val yesterdayDate = getYesterdayDate()
            val news = repository.getNews(from = yesterdayDate)?.map { newsItem ->
                // Generate ID for each news item
                newsItem.copy(id = generateNewsItemId(newsItem.title, newsItem.publishedAt))
            }

            // Apply the filter and store the news items
            _newsItems.value = news?.filter { isLatinAlphabet(it.title) }

            // Log each news item and its id
            _newsItems.value?.forEach { newsItem ->
                Log.d("FeedViewModel", "NewsItem: ${newsItem.title}, id: ${newsItem.id}")
            }
        }
    }

    private fun getYesterdayDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return dateFormat.format(calendar.time)
    }

    fun isLatinAlphabet(text: String): Boolean {
        return text.all { it in '\u0000'..'\u007F' }
    }

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
