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
import com.example.newsmobileapplication.viewmodel.CategoryViewModel

@Composable
fun CategoryScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel(),  // CategoryViewModel kullanılıyor
    onCategorySelected: (String) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }

    // Haberler ve yükleme durumu state'lerini alıyoruz
    val newsItems by viewModel.newsItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Ekran yüklendiğinde ilk olarak "Arts" kategorisi için API çağrısı yapılıyor
    LaunchedEffect(Unit) {
        viewModel.fetchNewsByCategory("World")
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Geri tuşu ve başlık
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Explore Categories",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Arama çubuğu
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = {
                // Arama yapıldığında, CategoryViewModel'den API çağrısı yapıyoruz
                viewModel.fetchNewsByCategory(query)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Search for news..."
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Kategori sekmesi
        CategoryScrollableRow { selectedCategory ->
            // Kategori seçildiğinde, CategoryViewModel'den API çağrısı yapıyoruz
            viewModel.fetchNewsByCategory(selectedCategory)
        }

        Spacer(modifier = Modifier.height(8.dp))

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
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(newsItems ?: emptyList()) { newsItem ->
                    CategoryCardComponent(
                        newsTitle = newsItem.title,
                        newsContent = newsItem.abstract ?: "",
                        imageUrl = newsItem.multimedia?.firstOrNull()?.url,
                        onClick = {
                            // Haber detayına gitme işlemi burada yapılabilir
                        }
                    )
                }
            }
        }
    }
}
