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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import com.example.newsmobileapplication.ui.theme.Platinum
import com.example.newsmobileapplication.ui.theme.Redwood
import com.example.newsmobileapplication.utils.formatDateTime

@Composable
fun FavoriteCardComponent(
    newsTitle: String,
    newsContent: String,
    newsSection: String,
    newsDate: String,
    newsAuthor: String,
    imageUrl: String?,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit // Callback for removing from favorites
) {
    Card(
        modifier = Modifier
            .padding(8.dp) // Card dışındaki padding
            .fillMaxWidth()
            .clickable { onClick() }
            .border(1.dp, Platinum, RoundedCornerShape(15.dp)), // Border ekleniyor
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp) // Card içerisindeki bileşenler için padding
        ) {
            // Image section with title and section over it
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp) // Görsel yüksekliği
                    .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                    .background(Color.Transparent)
            ) {
                if (imageUrl != null) {
                    Image(
                        painter = rememberImagePainter(imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize() // Tüm alanı kaplaması için
                            .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)),
                        contentScale = ContentScale.Crop // Görselin kesilmesini önlemek için
                    )

                    // Overlay for section and title
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Section
                        Text(
                            text = newsSection.uppercase(),
                            fontSize = 14.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(Redwood, shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Title
                        Text(
                            text = newsTitle,
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom section with date, author, and icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp), // Row ile üstteki bileşen arasında padding
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    // Date
                    Text(
                        text = formatDateTime(newsDate),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Author
                    Text(
                        text = newsAuthor,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // Favorite icon button
                IconButton(
                    onClick = onRemoveClick,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.LightGray.copy(0.5f), shape = RoundedCornerShape(50))
                        .size(32.dp) // Circle background size
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Remove from favorites",
                        tint = Redwood,
                        modifier = Modifier.size(18.dp) // Icon size suggestion
                    )
                }
            }

            // News Content
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = newsContent, // No need for a null check since it's a non-nullable String
                fontSize = 16.sp,
                color = Color.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
