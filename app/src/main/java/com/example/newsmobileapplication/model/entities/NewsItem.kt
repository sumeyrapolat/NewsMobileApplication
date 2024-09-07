package com.example.newsmobileapplication.model.entities

import com.google.gson.annotations.SerializedName

data class NewsItem(
    @SerializedName("title") val title: String,
    @SerializedName("abstract") val abstract: String?,
    @SerializedName("byline") val byline: String?,
    @SerializedName("published_date") val publishedDate: String,
    @SerializedName("url") val url: String,
    @SerializedName("multimedia") val multimedia: List<Multimedia>?,
    @SerializedName("section") val section: String,
    @SerializedName("id") val id: String
)

data class NewsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("section") val section: String,
    @SerializedName("num_results") val numResults: Int,
    @SerializedName("results") val results: List<NewsItem>
)

data class Multimedia(
    @SerializedName("url") val url: String?,
    @SerializedName("format") val format: String?,
    @SerializedName("height") val height: Int?,
    @SerializedName("width") val width: Int?,
    @SerializedName("type") val type: String?,
    @SerializedName("subtype") val subtype: String?,
    @SerializedName("caption") val caption: String?,
    @SerializedName("copyright") val copyright: String?
)
