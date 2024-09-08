package com.example.newsmobileapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.newsmobileapplication.ui.components.CategoryCardComponent
import com.example.newsmobileapplication.ui.components.CategoryScrollableRow
import com.example.newsmobileapplication.ui.components.SearchBar
import com.example.newsmobileapplication.ui.theme.Platinum
import com.example.newsmobileapplication.utils.formatDateTime
import com.example.newsmobileapplication.viewmodel.CategoryViewModel

@Composable
fun CategoryScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel(),
    onCategorySelected: (String) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }

    val newsItems by viewModel.newsItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Varsayılan olarak "World" kategorisini seçiyoruz
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

        Spacer(modifier = Modifier.height(6.dp)) // Kategorilerle arama çubuğu arasında boşluk

        // Search Bar
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = {
                viewModel.fetchNewsByCategory(query)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), // Arama çubuğu için dikeyde boşluk
            placeholder = "Search for news..."
        )

        Spacer(modifier = Modifier.height(8.dp)) // Kategorilerle arama çubuğu arasında boşluk

        // Kategori sekmesi
        CategoryScrollableRow { selectedCategory ->
            viewModel.fetchNewsByCategory(selectedCategory)
        }

        Spacer(modifier = Modifier.height(8.dp)) // Kategorilerle içerik arasında boşluk

        // Yükleniyorsa loading göstergesi
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Haberlere ait kartlar
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp), // Kartların kenar boşlukları
                verticalArrangement = Arrangement.spacedBy(10.dp) // Kartlar arasındaki boşluk
            ) {
                items(newsItems ?: emptyList()) { newsItem ->
                    CategoryCardComponent(
                        newsTitle = newsItem.title,
                        newsAuthor = "• " + newsItem.byline,
                        newsDate = formatDateTime(newsItem.publishedDate),
                        imageUrl = newsItem.multimedia?.firstOrNull()?.url,
                        onClick = {
                            navController.navigate("newsDetail/${newsItem.id}")
                        }
                    )
                }
            }
        }
    }
}
