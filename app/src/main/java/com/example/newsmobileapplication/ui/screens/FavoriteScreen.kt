package com.example.newsmobileapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.newsmobileapplication.ui.components.FavoriteCardComponent
import com.example.newsmobileapplication.viewmodel.FeedViewModel

@Composable
fun FavoriteScreen(navController: NavController, viewModel: FeedViewModel = hiltViewModel()) {
    // Favori haberleri state ile yönetiyoruz, favoriden çıkarıldıkça güncellenmesi için
    val favoriteNewsItems by viewModel.favoriteNewsItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Genel padding
    ) {
        // Ekranın üstüne başlık ekliyoruz
        Text(
            text = "Saved Articles",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,  // Siyah renk
            modifier = Modifier
                .padding(start =6.dp, bottom = 12.dp) // Başlık ve liste arasına boşluk ekliyoruz
        )

        // Favori haberlerin listesi
        LazyColumn {
            items(favoriteNewsItems) { newsItem ->
                // multimedia listesinden ilk görseli alıyoruz
                val imageUrl = newsItem.multimedia?.firstOrNull()?.url

                FavoriteCardComponent(
                    newsTitle = newsItem.title,
                    newsContent = newsItem.abstract ?: "No content available",  // abstract alanı kullanılıyor
                    newsSection = newsItem.section,
                    imageUrl = imageUrl,
                    onClick = {
                        // Habere tıklayınca detay sayfasına gitme işlemi
                        navController.navigate("newsDetail/${newsItem.id}")
                    },
                    onRemoveClick = {
                        // Favorilerden çıkarma işlemi
                        viewModel.toggleFavorite(newsItem.id)  // Favori durumunu değiştiriyoruz
                    }
                )
            }
        }
    }
}
