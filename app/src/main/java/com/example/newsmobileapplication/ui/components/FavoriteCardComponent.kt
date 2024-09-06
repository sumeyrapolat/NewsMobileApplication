package com.example.newsmobileapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import com.example.newsmobileapplication.ui.theme.KhasmirBlue
import com.example.newsmobileapplication.ui.theme.Platinum
import com.example.newsmobileapplication.ui.theme.Redwood

@Composable
fun FavoriteCardComponent(
    newsTitle: String,
    newsContent: String,
    newsSection: String,
    imageUrl: String?,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit // Favorilerden çıkarmak için callback
) {
    Card(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .clickable { onClick() }
            .border(1.dp, Platinum, RoundedCornerShape(15.dp)), // Card için ince gri border
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Görsel ve metinler dikeyde ortalanacak
        ) {
            // Sol taraftaki Column: Görsel ve Kategori
            Column(
                modifier = Modifier.weight(2f) // Sol tarafın genişliğini ayarlıyoruz
            ) {
                // Görsel eklendi
                if (imageUrl != null) {
                    Image(
                        painter = rememberImagePainter(data = imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp) // Görselin genişlik ve yüksekliği sabit
                            .clip(RoundedCornerShape(10.dp)) // Görselin kenarları yuvarlanıyor
                            .padding(end = 8.dp), // Görsel ile içerik arasında boşluk
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Kategori kutucuğu
                Box(
                    modifier = Modifier
                        .background(
                            Redwood,
                            shape = RoundedCornerShape(12.dp)
                        ) // Kırmızı arka plan ve yuvarlak köşeler
                        .padding(horizontal = 12.dp, vertical = 3.dp) // İç kenar boşluğu
                ) {
                    Text(
                        text = newsSection,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Sağ taraftaki Column: Başlık, Favori butonu ve içerik
            Column(
                modifier = Modifier.weight(5f) // Sağ tarafın genişliğini ayarlıyoruz
            ) {
                // Başlık ve favori ikonu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Haber başlığı
                    Text(
                        text = newsTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    // Favori butonu
                    IconButton(onClick = onRemoveClick) {
                        Icon(
                            imageVector = Icons.Default.Bookmark ,
                            contentDescription = "Remove from favorites",
                            tint = Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Haber özeti (abstract)
                Text(
                    text = newsContent,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
