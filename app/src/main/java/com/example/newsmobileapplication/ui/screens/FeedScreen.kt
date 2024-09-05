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
                val newsItemId = generateNewsItemId(newsItem.title, newsItem.publishedAt)
                NewsCardComponent(
                    newsTitle = newsItem.title,
                    newsSummary = newsItem.description ?: "No summary available",
                    newsImageUrl = newsItem.urlToImage ?: "",
                    onClick = {
                        // Navigate to NewsDetailScreen with the generated ID
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
