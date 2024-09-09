package com.example.newsmobileapplication.model.entities

import com.google.gson.annotations.SerializedName

data class ArticleSearchResponse(
    @SerializedName("status") val status: String,
    @SerializedName("response") val response: ArticleSearchResult
)

data class ArticleSearchResult(
    @SerializedName("docs") val docs: List<Article>
)

data class Article(
    @SerializedName("headline") val headline: Headline,
    @SerializedName("byline") val byline: Byline,
    @SerializedName("pub_date") val pubDate: String,
    @SerializedName("multimedia") val multimedia: List<ArticleMultimedia>?,
    @SerializedName("lead_paragraph") val leadParagraph: String?,
    @SerializedName("web_url") val webUrl: String,
    @SerializedName("id") val id: String
)


data class Headline(
    @SerializedName("main") val main: String
)

data class Byline(
    @SerializedName("original") val original: String
)

data class ArticleMultimedia( // İsim değiştirildi
    @SerializedName("url") val urlArticle: String
)
