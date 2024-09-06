package com.example.newsmobileapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmobileapplication.model.entities.NewsItem
import com.example.newsmobileapplication.model.repository.FeedRepository
import com.example.newsmobileapplication.utils.generateNewsItemId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: FeedRepository  // Haberleri API'den çekmek için repository
) : ViewModel() {

    private val _newsItems = MutableStateFlow<List<NewsItem>?>(null)
    val newsItems: StateFlow<List<NewsItem>?> = _newsItems

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Kategoriye göre haberleri çekmek için fonksiyon
    fun fetchNewsByCategory(section: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val news = repository.getNews(section)?.map { newsItem ->
                    val generatedId = generateNewsItemId(newsItem.url)
                    newsItem.copy(id = generatedId)
                }
                _newsItems.value = news
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching news: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
