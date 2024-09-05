package com.example.newsmobileapplication.model.entities


data class NewsItem(
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val content: String?,
    val author: String?,
    val publishedAt: String,
    val id: String // ID must be provided
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
