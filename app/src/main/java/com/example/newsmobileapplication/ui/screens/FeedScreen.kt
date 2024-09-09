package com.example.newsmobileapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.newsmobileapplication.model.entities.NewsItem
import com.example.newsmobileapplication.ui.components.NewsCardComponent
import com.example.newsmobileapplication.ui.components.TopNewsScrollableRow
import com.example.newsmobileapplication.utils.ApiResult
import com.example.newsmobileapplication.utils.formatDateTime
import com.example.newsmobileapplication.utils.generateNewsItemId
import com.example.newsmobileapplication.viewmodel.FeedViewModel

@Composable
fun FeedScreen(navController: NavController, viewModel: FeedViewModel = hiltViewModel()) {
    val newsState by viewModel.newsItems.collectAsState() // Using ApiResult


    Box(modifier = Modifier.fillMaxSize()) {
        when (newsState) {
            is ApiResult.Loading -> {
                // Show loading spinner
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ApiResult.Success -> {
                val newsItems = (newsState as ApiResult.Success<List<NewsItem>>).data
                val sortedNewsItems = newsItems.sortedByDescending { it.publishedDate }
                val topNewsItems = sortedNewsItems.take(5)
                val recommendationItems = sortedNewsItems.drop(5)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                ) {
                    // Breaking News (En son yayınlanan ilk 5 haber)
                    Text(
                        text = "Breaking News",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )

                    if (topNewsItems.isNotEmpty()) {
                        // Scrollable Row with Top 5 News
                        TopNewsScrollableRow(
                            topNewsItems = topNewsItems,
                            onNewsClick = { newsItem ->
                                val newsItemId = generateNewsItemId(newsItem.url)
                                Log.d("FeedScreen", "Breaking News Clicked ID: $newsItemId") // Log ID
                                navController.navigate("newsDetail/$newsItemId")
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Recommendation (İlk 5 haricindeki haberler)
                    Text(
                        text = "Recommendation",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )

                    LazyColumn {
                        items(recommendationItems) { newsItem ->
                            val newsItemId = generateNewsItemId(newsItem.url)
                            val newsImageUrl = newsItem.multimedia?.firstOrNull()?.url ?: ""

                            NewsCardComponent(
                                newsTitle = newsItem.title,
                                imageUrl = newsImageUrl,
                                newsSection = newsItem.section,
                                newsDate = formatDateTime(newsItem.publishedDate),
                                newsAuthor = "• " + newsItem.byline,
                                onClick = {
                                    Log.d("FeedScreen", "Recommendation News Clicked ID: $newsItemId") // Log ID
                                    navController.navigate("newsDetail/$newsItemId")
                                }
                            )
                        }
                    }
                }
            }

            is ApiResult.Error -> {
                // Handle error state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Error loading news", color = Color.Red)
                }
            }
        }
    }
}
