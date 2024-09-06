package com.example.newsmobileapplication.ui.screens


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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.newsmobileapplication.ui.components.NewsCardComponent
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
        // Show the list of news when loading is complete
        LazyColumn {
            if (newsItems != null) {
                items(newsItems!!) { newsItem ->
                    val newsItemId = generateNewsItemId(newsItem.url)
                    val newsImageUrl = newsItem.multimedia?.firstOrNull()?.url ?: ""

                    NewsCardComponent(
                        newsTitle = newsItem.title,
                        newsSummary = newsItem.abstract ?: "No summary available",
                        newsImageUrl = newsImageUrl,
                        onClick = {
                            navController.navigate("newsDetail/$newsItemId")
                        }
                    )
                }
            } else {
                // Show a friendly message when no news is available
                item {
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
}
