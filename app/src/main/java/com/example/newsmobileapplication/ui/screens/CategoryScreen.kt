package com.example.newsmobileapplication.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.newsmobileapplication.model.entities.Article
import com.example.newsmobileapplication.model.entities.NewsItem
import com.example.newsmobileapplication.ui.components.CategoryCardComponent
import com.example.newsmobileapplication.ui.components.CategoryScrollableRow
import com.example.newsmobileapplication.ui.components.SearchBar
import com.example.newsmobileapplication.ui.components.SearchCardComponent
import com.example.newsmobileapplication.utils.ApiResult
import com.example.newsmobileapplication.utils.formatDateTime
import com.example.newsmobileapplication.viewmodel.CategoryViewModel

@Composable
fun CategoryScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel(),
    onCategorySelected: (String) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    val newsItems by viewModel.newsItems.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    // Varsayılan olarak kategoriye ait haberleri getiriyoruz
    LaunchedEffect(Unit) {
        viewModel.fetchNewsByCategory("World")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Discover Başlık
        Text(
            text = "Discover",
            fontSize = 40.sp, // Büyük font
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp) // Başlığın altında boşluk
        )

        Spacer(modifier = Modifier.height(6.dp)) // Başlık ve alt başlık arası boşluk

        // Discover Alt Başlık
        Text(
            text = "News from all around the world",
            fontSize = 14.sp, // Alt başlık daha küçük
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray, // Gri renkte alt başlık
            modifier = Modifier.padding(horizontal = 16.dp) // Alt başlığın altında boşluk
        )

        Spacer(modifier = Modifier.height(6.dp)) // Arama çubuğu ve başlık arası boşluk

        // Search Bar
        SearchBar(
            query = query,
            onQueryChange = {
                query = it
                isSearchActive = query.isNotEmpty() // Eğer query boş değilse, arama aktif
            },
            onSearch = {
                if (query.isNotEmpty()) {
                    viewModel.fetchArticlesByQuery(query) // Arama sonuçlarını çekiyoruz
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), // Arama çubuğu için dikeyde boşluk
            placeholder = "Search for news..."
        )

        Spacer(modifier = Modifier.height(8.dp)) // Kategorilerle arama çubuğu arasında boşluk

        // Kategori sekmesi (arama aktif değilken görünür)
        if (!isSearchActive) {
            CategoryScrollableRow { selectedCategory ->
                viewModel.fetchNewsByCategory(selectedCategory)
            }

            Spacer(modifier = Modifier.height(8.dp)) // Kategorilerle içerik arasında boşluk
        }

        // Eğer arama aktifse, arama sonuçlarını göster
        if (isSearchActive) {
            when (searchResults) {
                is ApiResult.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is ApiResult.Success -> {
                    val results = (searchResults as ApiResult.Success<List<Article>>).data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(results) { result ->
                            val imageUrl = result.multimedia?.firstOrNull()?.urlArticle?.let {
                                "https://static01.nyt.com/$it"
                            }

                            SearchCardComponent(
                                newsTitle = result.headline.main,
                                newsAuthor = "• " + (result.byline.original ?: "Unknown author"),
                                imageUrl = imageUrl,
                                onClick = {
                                    navController.navigate("articleNewsDetail/${result.id}")
                                }
                            )
                        }
                    }
                }

                is ApiResult.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        val errorMessage = (searchResults as ApiResult.Error).exception.message
                        Text(text = errorMessage ?: "Error fetching search results", color = Color.Red)
                    }
                }
            }
        } else {
            when (newsItems) {
                is ApiResult.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is ApiResult.Success -> {
                    // Kategori haberlerini yayın tarihine göre sıralıyoruz
                    val newsList = (newsItems as ApiResult.Success<List<NewsItem>>).data.sortedByDescending { it.publishedDate }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(newsList) { newsItem ->
                            val imageUrl = newsItem.multimedia?.firstOrNull()?.url

                            CategoryCardComponent(
                                newsTitle = newsItem.title,
                                newsAuthor = "• " + newsItem.byline,
                                newsDate = formatDateTime(newsItem.publishedDate),
                                imageUrl = imageUrl,
                                onClick = {
                                    navController.navigate("newsDetail/${newsItem.id}")
                                }
                            )
                        }
                    }
                }

                is ApiResult.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        val errorMessage = (newsItems as ApiResult.Error).exception.message
                        Text(text = errorMessage ?: "Error fetching news", color = Color.Red)
                    }
                }
            }
        }
    }
}
