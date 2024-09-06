package com.example.newsmobileapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.newsmobileapplication.viewmodel.FeedViewModel

@Composable
fun NewsDetailScreen(
    navController: NavController,
    newsItemId: String,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val newsItem = viewModel.getNewsItemById(newsItemId)
    val isFavorite = remember { mutableStateOf(viewModel.isFavorite(newsItemId)) }

    if (newsItem == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Loading news details...", color = Color.Gray, fontSize = 18.sp)
        }
    } else {
        // Feed ekranındaki gibi görseli multimedia'dan alalım
        val newsImageUrl = newsItem.multimedia?.firstOrNull()?.url

        // Detay ekranını oluşturuyoruz
        Box(modifier = Modifier.fillMaxSize()) {
            if (newsImageUrl!!.isNotEmpty()) {
                Image(
                    painter = rememberImagePainter(data = newsImageUrl),
                    contentDescription = "News Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = 0.8f },
                    contentScale = ContentScale.Crop
                )
            }

            // Geri butonu
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(50))
                    .size(40.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }

            // Favorilere ekleme butonu
            IconButton(
                onClick = {
                    viewModel.toggleFavorite(newsItemId)
                    isFavorite.value = !isFavorite.value
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
                    .background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(50))
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = if (isFavorite.value) Color.Red else Color.Black
                )
            }

            // Haber detaylarının bulunduğu kart
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    item {
                        // Haber başlığı
                        Text(
                            text = newsItem.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Yazar bilgisi
                        Text(
                            text = "By ${newsItem.byline ?: "Unknown author"}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Haber özeti (abstract)
                        Text(
                            text = newsItem.abstract ?: "No summary available",
                            fontSize = 16.sp,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Haber detaylarına gitmek için URL
                        Text(
                            text = "Read more: ${newsItem.url}",
                            fontSize = 14.sp,
                            color = Color.Blue
                        )
                    }
                }
            }
        }
    }
}
