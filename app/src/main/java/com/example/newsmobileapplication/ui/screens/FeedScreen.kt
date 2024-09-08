package com.example.newsmobileapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import com.example.newsmobileapplication.ui.components.NewsCardComponent
import com.example.newsmobileapplication.ui.components.TopNewsScrollableRow
import com.example.newsmobileapplication.ui.theme.Platinum
import com.example.newsmobileapplication.utils.formatDateTime
import com.example.newsmobileapplication.utils.generateNewsItemId
import com.example.newsmobileapplication.viewmodel.FeedViewModel

@Composable
fun FeedScreen(navController: NavController, viewModel: FeedViewModel = hiltViewModel()) {
    val newsItems by viewModel.newsItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Display error message if any
    if (errorMessage != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = errorMessage ?: "Unknown error", color = Color.Red)
        }
    } else if (isLoading) {
        // Show loading spinner
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Show the feed content
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(start= 12.dp, end = 12.dp, bottom = 12.dp) // Genel padding
        ) {
            // Breaking News (Top 5 News in a Scrollable Row)
            Text(
                text = "Breaking News",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            if (newsItems != null && newsItems!!.isNotEmpty()) {
                val topNewsItems = newsItems!!.take(5) // İlk 5 haberi alıyoruz

                // Scrollable Row with Top 5 News
                TopNewsScrollableRow(
                    topNewsItems = topNewsItems,
                    onNewsClick = { newsItem ->
                        val newsItemId = generateNewsItemId(newsItem.url)
                        navController.navigate("newsDetail/$newsItemId")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Recommendation (Remaining News)
                Text(
                    text = "Recommendation",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )

                LazyColumn {
                    items(newsItems!!.drop(5)) { newsItem -> // 6. haberi başlat
                        val newsItemId = generateNewsItemId(newsItem.url)
                        val newsImageUrl = newsItem.multimedia?.firstOrNull()?.url ?: ""

                        NewsCardComponent(
                            newsTitle = newsItem.title,
                            imageUrl = newsImageUrl,
                            newsSection = newsItem.section,
                            newsDate = formatDateTime(newsItem.publishedDate),
                            newsAuthor = "• " + newsItem.byline,
                            onClick = {
                                navController.navigate("newsDetail/$newsItemId")
                            }
                        )
                    }
                }
            } else {
                // Show a friendly message when no news is available
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No news available.")
                }
            }
        }
    }
}


