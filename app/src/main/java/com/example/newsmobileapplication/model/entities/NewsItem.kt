package com.example.newsmobileapplication.model.entities

data class NewsItem(
    val title: String,       // Haber başlığı
    val description: String?, // Haber özeti (null olabilir)
    val urlToImage: String?,  // Haber görseli URL'si
    val content: String?,     // Haber içeriği (Detay sayfası için)
    val url: String           // Haber kaynağının orijinal URL'si
)

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsItem>
)

data class Source(
    val id: String?,
    val name: String
)
