package com.example.newsmobileapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    val favoriteNewsItems by viewModel.favoriteNewsItems.collectAsState()
    val userName by viewModel.userName.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
    ) {

        Text(
            text = "Saved Articles for ${userName ?: "User"}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .padding(8.dp)
        )

        if (favoriteNewsItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No saved articles.",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        } else {
            val sortedFavorites = favoriteNewsItems.sortedByDescending { it.publishedDate }

            LazyColumn {
                items(sortedFavorites) { newsItem ->
                    val imageUrl = newsItem.multimedia?.firstOrNull()?.url

                    FavoriteCardComponent(
                        newsTitle = newsItem.title,
                        newsContent = newsItem.abstract ?: "No content available",
                        newsSection = newsItem.section,
                        newsDate = newsItem.publishedDate,
                        newsAuthor = "• " + newsItem.byline,
                        imageUrl = imageUrl,
                        onClick = {
                            navController.navigate("newsDetail/${newsItem.id}")
                        },
                        onRemoveClick = {
                            viewModel.toggleFavorite(newsItem.id)
                        }
                    )
                }
            }
        }
    }
}
