package com.example.newsmobileapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.newsmobileapplication.model.entities.NewsItem
import com.example.newsmobileapplication.utils.ApiResult
import com.example.newsmobileapplication.viewmodel.FeedViewModel

@Composable
fun NewsDetailScreen(
    navController: NavController,
    newsItemId: String,
    viewModel: FeedViewModel = hiltViewModel()
) {
    Log.d("NewsDetailScreen", "News Item ID passed: $newsItemId")

    val newsItemsState by viewModel.newsItems.collectAsState()
    val isFavorite = remember { mutableStateOf(viewModel.isFavorite(newsItemId)) }

    LaunchedEffect(newsItemsState) {
        if (newsItemsState is ApiResult.Success) {
            Log.d("NewsDetailScreen", "News items successfully loaded, fetching news detail for ID: $newsItemId")
            viewModel.fetchNewsItemById(newsItemId)
        } else {
            Log.d("NewsDetailScreen", "News items not loaded yet or error occurred")
        }
    }

    val newsDetailState by viewModel.newsDetail.collectAsState()

    when (newsDetailState) {
        is ApiResult.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Loading news details...", color = Color.DarkGray, fontSize = 18.sp)
            }
        }

        is ApiResult.Success -> {
            val newsItem = (newsDetailState as ApiResult.Success<NewsItem>).data

            if (newsItem.id == newsItemId) {
                val newsImageUrl = newsItem.multimedia?.firstOrNull()?.url

                Box(modifier = Modifier.fillMaxSize()) {
                    if (!newsImageUrl.isNullOrEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = newsImageUrl),
                            contentDescription = "News Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { alpha = 0.8f },
                            contentScale = ContentScale.Crop
                        )
                    }

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

                    // Favori butonu
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
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Favorite",
                            tint = if (isFavorite.value) Color.Red else Color.Black
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp, start = 2.dp, end = 2.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))

                        Box(
                            modifier = Modifier
                                .background(Color(0xFF607D8B), shape = RoundedCornerShape(12.dp))
                                .padding(horizontal = 15.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = newsItem.section.uppercase(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            LazyColumn(modifier = Modifier.padding(16.dp)) {
                                item {
                                    Text(
                                        text = newsItem.title,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = newsItem.byline ?: "Unknown author",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = newsItem.abstract ?: "No summary available",
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

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
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "News Item Not Found", color = Color.Red, fontSize = 18.sp)
                }
            }
        }

        is ApiResult.Error -> {
            val errorMessage = (newsDetailState as ApiResult.Error).exception.message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage ?: "Error loading news details", color = Color.Red, fontSize = 18.sp)
            }
        }
    }
}
