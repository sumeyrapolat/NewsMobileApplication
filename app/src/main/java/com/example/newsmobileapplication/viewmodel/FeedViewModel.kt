package com.example.newsmobileapplication.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmobileapplication.model.entities.NewsItem
import com.example.newsmobileapplication.model.repository.FeedRepository
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
    private val repository: FeedRepository
) : ViewModel() {

    private val _newsItems = MutableStateFlow<List<NewsItem>?>(null)
    val newsItems: StateFlow<List<NewsItem>?> = _newsItems

    init {
        fetchNews()
    }

    // Haberleri API'dan çek
    private fun fetchNews() {
        viewModelScope.launch {
            val yesterdayDate = getYesterdayDate() // Son 24 saatin tarihini al
            val news = repository.getNews(from = yesterdayDate) // API'den haberleri çek
            _newsItems.value = news?.filter { isLatinAlphabet(it.title) } // Filter only news with Latin titles
        }
    }


    // Son 24 saatin tarihini döndür
    private fun getYesterdayDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1) // Bir gün geri git
        return dateFormat.format(calendar.time)
    }

    fun isLatinAlphabet(text: String): Boolean {
        return text.all { it in '\u0000'..'\u007F' } // Checks if all characters are within the basic Latin Unicode block
    }

}
