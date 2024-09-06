package com.example.newsmobileapplication.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.newsmobileapplication.ui.components.NewsCardComponent
import com.example.newsmobileapplication.utils.generateNewsItemId
import com.example.newsmobileapplication.viewmodel.FeedViewModel


@Composable
fun FeedScreen(navController: NavController, viewModel: FeedViewModel = hiltViewModel()) {
    val newsItems by viewModel.newsItems.collectAsState()

    LazyColumn {
        if (newsItems != null) {
            items(newsItems!!) { newsItem ->
                // Haberlerin URL'sine göre benzersiz bir ID oluşturuyoruz
                val newsItemId = generateNewsItemId(newsItem.url)

                // İlk görseli almak için multimedia listesini kontrol edin
                val newsImageUrl = newsItem.multimedia?.firstOrNull()?.url ?: ""

                NewsCardComponent(
                    newsTitle = newsItem.title,
                    newsSummary = newsItem.abstract ?: "No summary available", // 'abstract' özeti temsil ediyor
                    newsImageUrl = newsImageUrl, // İlk görselin URL'si alınıyor
                    onClick = {
                        // Haber detay sayfasına URL üzerinden yönlendirme yapıyoruz
                        navController.navigate("newsDetail/$newsItemId")
                    }
                )
            }
        } else {
            item {
                Text("Loading news...")
            }
        }
    }
}
