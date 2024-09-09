package com.example.newsmobileapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
fun CategoryCardComponent(
    newsTitle: String,
    newsDate: String,
    newsAuthor: String,
    imageUrl: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
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
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Row'un içeriği dikeyde ortalanır
        ) {
            // Görsel eklendi
            if (imageUrl != null) {
                Image(
                    painter = rememberImagePainter(data = imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(2f) // Görsel 2 birim yer kaplayacak
                        .size(120.dp) // Görselin genişlik ve yüksekliği sabit
                        .padding(end = 8.dp) // Görsel ile içerik arasında boşluk
                        .clip(RoundedCornerShape(10.dp)), // Görselin kenarları yuvarlanıyor
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Başlık ve içerik için column
            Column(
                modifier = Modifier.weight(5f) // Başlık ve içerik 5 birim yer kaplayacak
            ) {
                // Haber özeti (abstract)
                Text(
                    text = newsAuthor,
                    fontSize = 14.sp,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Haber başlığı
                Text(
                    text = newsTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = newsDate,
                    fontSize = 14.sp,
                    color = Color.Gray,
                )
            }
        }
    }


}
