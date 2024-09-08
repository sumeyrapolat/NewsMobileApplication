package com.example.newsmobileapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsmobileapplication.ui.theme.KhasmirBlue
import com.example.newsmobileapplication.ui.theme.Redwood

@Composable
fun CategoryScrollableRow(onCategorySelected: (String) -> Unit) {
    val categories = listOf(
        "World", "Politics", "Business", "Technology", "Health",
        "Sports", "Movies", "Fashion", "Science", "Travel", "Arts", "Upshot"
    )
    var selectedCategoryIndex by remember { mutableStateOf(0) } // Default olarak World seçili

    ScrollableTabRow(
        selectedTabIndex = selectedCategoryIndex,
        edgePadding = 12.dp,
        backgroundColor = Color.White,
        contentColor = KhasmirBlue,
        indicator = { Box {} }, // Boş Box ile indicator'ı kaldırıyoruz
        divider = { Box {} }    // Divider'ı kaldırıyoruz
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = selectedCategoryIndex == index,
                onClick = {
                    selectedCategoryIndex = index
                    onCategorySelected(category) // Seçilen kategoriyi çağırıyoruz
                },
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 4.dp)
                    .clip(RoundedCornerShape(22.dp)) // Kenarları yuvarlak
                    .background(
                        if (selectedCategoryIndex == index) KhasmirBlue else Color.Transparent
                    ), // Seçili olan kutucuğun arka planını belirliyoruz
                text = {
                    Text(
                        text = category,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = if (selectedCategoryIndex == index) Color.White else Color.Gray, // Seçili olanın rengi beyaz
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
                    )
                }
            )
        }
    }

}
