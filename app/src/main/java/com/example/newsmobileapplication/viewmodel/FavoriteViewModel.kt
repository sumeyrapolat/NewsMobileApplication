package com.example.newsmobileapplication.viewmodel

import com.example.newsmobileapplication.model.entities.NewsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoriteViewModel(){
    private val _favoriteNews = MutableStateFlow<List<NewsItem>>(emptyList())
    val favoriteNews: StateFlow<List<NewsItem>> = _favoriteNews
}