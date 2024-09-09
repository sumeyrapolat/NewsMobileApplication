package com.example.newsmobileapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.newsmobileapplication.model.entities.Article
import com.example.newsmobileapplication.utils.ApiResult
import com.example.newsmobileapplication.viewmodel.CategoryViewModel

@Composable
fun SearchNewsDetailScreen(
    navController: NavController,
    articleId: String,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    Log.d("SearchNewsDetailScreen", "Article ID passed to detail screen: $articleId")

    // Observe the search results from the ViewModel
    val searchResults by viewModel.searchResults.collectAsState()

    when (searchResults) {
        is ApiResult.Loading -> {
            // Display loading state if data is being fetched
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Loading article details...", color = Color.DarkGray, fontSize = 18.sp)
            }
        }

        is ApiResult.Success -> {
            // Get the article matching the passed articleId from the successful results
            val searchArticle = (searchResults as ApiResult.Success<List<Article>>).data.find { it.id == articleId }

            if (searchArticle != null) {
                // Display article details
                val articleImageUrl = searchArticle.multimedia?.firstOrNull()?.urlArticle?.let {
                    "https://static01.nyt.com/$it"
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    // Display the article image if available
                    if (!articleImageUrl.isNullOrEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = articleImageUrl),
                            contentDescription = "Article Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { alpha = 0.8f },
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Back button
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

                    // Main content: display the article details
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
                                text = "SEARCH RESULT",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Article details in a card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            LazyColumn(modifier = Modifier.padding(16.dp)) {
                                item {
                                    // Article title
                                    Text(
                                        text = searchArticle.headline.main,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Article byline (author)
                                    Text(
                                        text = searchArticle.byline.original ?: "Unknown author",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Article summary
                                    Text(
                                        text = searchArticle.leadParagraph ?: "Unknown summary",
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // External link to full article
                                    Text(
                                        text = "Read more: ${searchArticle.webUrl}",
                                        fontSize = 14.sp,
                                        color = Color.Blue
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // If article not found in the results, show a not found message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Article not found.", color = Color.Red, fontSize = 18.sp)
                }
            }
        }

        is ApiResult.Error -> {
            // Show an error message if fetching search results failed
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val errorMessage = (searchResults as ApiResult.Error).exception.message
                Text(text = errorMessage ?: "Failed to load article details", color = Color.Red, fontSize = 18.sp)
            }
        }
    }
}
