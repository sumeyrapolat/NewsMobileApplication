package com.example.newsmobileapplication.model.entities

data class NewsItem(
    val title: String,                // Article title
    val abstract: String?,            // Summary/description of the article (NY Times uses 'abstract')
    val byline: String?,              // Author of the article
    val published_date: String,       // Published date of the article
    val url: String,                  // URL of the article
    val multimedia: List<Multimedia>?,// List of multimedia (images)
    val section: String,              // Section of the article (e.g. 'Technology')
    val id: String                    // Generated unique ID
)

data class NewsResponse(
    val status: String,               // Status of the response
    val section: String,              // The section of the articles (e.g. 'technology')
    val num_results: Int,             // Number of results returned
    val results: List<NewsItem>       // List of articles
)

data class Multimedia(
    val url: String?,                 // URL of the multimedia (image)
    val format: String?,              // Format/type of multimedia (e.g. 'Standard Thumbnail')
    val height: Int?,                 // Height of the image
    val width: Int?,                  // Width of the image
    val type: String?,                // Type of media (e.g. image)
    val subtype: String?,             // Subtype of media (e.g. photo)
    val caption: String?,             // Caption of the multimedia
    val copyright: String?            // Copyright information
)
