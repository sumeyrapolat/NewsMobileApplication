package com.example.newsmobileapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.newsmobileapplication.ui.theme.Platinum

@Composable
fun NewsCardComponent(
    newsTitle: String,
    newsSection: String,
    newsAuthor: String?,
    newsDate: String,
    imageUrl: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(12.dp)
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
            // Sol taraftaki Görsel
            if (imageUrl != null) {
                Image(
                    painter = rememberImagePainter(data = imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp) // Görselin genişlik ve yüksekliği sabit
                        .padding(end = 8.dp) // Görsel ile içerik arasında boşluk
                    .clip(RoundedCornerShape(10.dp)), // Görselin kenarları yuvarlanıyor
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Sağ taraftaki Column: Section, Başlık ve Yazar bilgisi
            Column(
                modifier = Modifier
                    .fillMaxWidth() // Column'un tüm genişliği kaplaması için
            ) {
                // Yazar bilgisi (Opsiyonel)
                if (!newsAuthor.isNullOrEmpty()) {
                    Text(
                        text = "$newsAuthor",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Başlık
                Text(
                    text = newsTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp) // Alt boşluk
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp), // Alt boşluk
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Section (Kategori)
                    Text(
                        text = newsSection,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray, // Kategoriyi farklı bir renkle ayırabiliriz
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = newsDate,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray, // Kategoriyi farklı bir renkle ayırabiliriz
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}
