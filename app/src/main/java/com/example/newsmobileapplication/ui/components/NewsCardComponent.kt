package com.example.newsmobileapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
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
    newsSummary: String,
    newsImageUrl: String,
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
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            // Görselin doğrudan yuvarlatılmış köşelerle ve sabit bir boyutta verilmesi
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp)) // Görselin kenarlarını yuvarlat
                    .background(Color.White) // Görsel yüklenmediğinde arka plan rengi
            ) {
                Image(
                    painter = rememberImagePainter(data = newsImageUrl),
                    contentDescription = "News Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f) // Görselin sabit en boy oranı (örneğin 16:9)
                        .clip(RoundedCornerShape(15.dp)), // Görselin köşelerini yuvarlat
                    // Görselin tamamen alanı doldurması için içerik ölçeklendirme ayarı
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title with no maxLines limit
            Text(
                text = newsTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Summary (abstract) with maxLines = 2
            Text(
                text = newsSummary,
                fontSize = 16.sp,
                color = Color.DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
