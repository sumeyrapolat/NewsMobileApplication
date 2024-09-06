package com.example.newsmobileapplication.ui.components

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
import androidx.compose.material.icons.filled.Favorite
import com.example.newsmobileapplication.ui.theme.KhasmirBlue
import com.example.newsmobileapplication.ui.theme.Platinum
import com.example.newsmobileapplication.ui.theme.Redwood

@Composable
fun FavoriteCardComponent(
    newsTitle: String,
    newsContent: String,
    newsSection:String,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit // Favorilerden çıkarmak için callback
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
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Row içinde başlık ve favori ikonu
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
                        imageVector = Icons.Default.Favorite,
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

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .background(
                        Redwood,
                        shape = RoundedCornerShape(12.dp)
                    ) // Mavi arka plan ve yuvarlak köşeler
                    .padding(horizontal = 12.dp, vertical =3.dp) // İç kenar boşluğu
            ) {
                Text(
                    text = newsSection,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

        }
    }
}
