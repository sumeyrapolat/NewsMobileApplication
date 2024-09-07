package com.example.newsmobileapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.newsmobileapplication.model.entities.NewsItem
import com.example.newsmobileapplication.ui.theme.Redwood
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun TopNewsScrollableRow(
    topNewsItems: List<NewsItem>, // List of top news items
    onNewsClick: (NewsItem) -> Unit
) {
    val listState = rememberLazyListState() // Kaydırma durumunu izlemek için LazyListState
    val coroutineScope = rememberCoroutineScope()
    var currentIndex by remember { mutableStateOf(0) } // Aktif indexi izlemek için
    val density = LocalDensity.current // LocalDensity kullanarak dp to px dönüşümü

    // Kart boyutları ve boşlukların px'e dönüştürülmesi
    val itemWidthPx = with(density) { 300.dp.toPx() }
    val itemSpacingPx = with(density) { 6.dp.toPx() }

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(topNewsItems) { newsItem ->
                TopNewsCard(
                    newsItem = newsItem,
                    onClick = { onNewsClick(newsItem) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Göstergeler (dots)
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(topNewsItems.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (index == currentIndex) Color.Black else Color.LightGray)
                        .clickable {
                            coroutineScope.launch {
                                listState.animateScrollToItem(index) // Dot tıklandığında o sıradaki habere geçiyoruz
                            }
                        }
                )
            }
        }

        // Kaydırma durumu izleniyor ve currentIndex güncelleniyor
        LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
            val visibleItemIndex = listState.firstVisibleItemIndex
            val visibleItemOffset = listState.firstVisibleItemScrollOffset.toFloat()
            val totalItemWidth = itemWidthPx + itemSpacingPx
            val offsetPercentage = visibleItemOffset / totalItemWidth

            currentIndex = (visibleItemIndex + offsetPercentage.roundToInt()).coerceIn(0, topNewsItems.size - 1)
        }
    }
}

@Composable
fun TopNewsCard(newsItem: NewsItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(300.dp) // Card boyutu ayarlanıyor
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp) // Köşeleri yuvarlatılmış card
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Haber görseli
            if (newsItem.multimedia?.firstOrNull()?.url != null) {
                Image(
                    painter = rememberImagePainter(newsItem.multimedia.firstOrNull()?.url),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)), // Köşeler yuvarlanıyor
                    contentScale = ContentScale.Crop // Görselin kesilmesini önlemek için Crop
                )
            }
            // Üst katman: Section ve Başlık
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)) // Görsel üzerinde yarı saydam arka plan
                    .padding(8.dp),
                verticalArrangement = Arrangement.Bottom, // En alta yerleştiriyoruz
                horizontalAlignment = Alignment.Start
            ) {
                // Section
                Text(
                    text = newsItem.section.uppercase(),
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Redwood, shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Başlık
                Text(
                    text = newsItem.title,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
