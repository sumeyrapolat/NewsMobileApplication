package com.example.newsmobileapplication.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.newsmobileapplication.ui.components.FavoriteCardComponent
import com.example.newsmobileapplication.viewmodel.FeedViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FavoriteScreen(navController: NavController, viewModel: FeedViewModel = hiltViewModel()) {
    val favoriteNewsItems = viewModel.getFavoriteNewsItems()

    LazyColumn {
        items(favoriteNewsItems) { newsItem ->
            // multimedia listesinden ilk görseli alıyoruz
            val imageUrl = newsItem.multimedia?.firstOrNull()?.url

            FavoriteCardComponent(
                newsTitle = newsItem.title,
                newsContent = newsItem.abstract ?: "No content available",  // abstract alanı kullanılıyor
                newsImageUrl = imageUrl ?: "",  // multimedia listesindeki görseli kullan
                onClick = {
                    // Habere tıklayınca detay sayfasına gitme işlemi
                    navController.navigate("newsDetail/${newsItem.id}")
                },
                onRemoveClick = {
                    // Favorilerden çıkarma işlemi
                    viewModel.removeFavorite(newsItem.id)
                }
            )
        }
    }
}