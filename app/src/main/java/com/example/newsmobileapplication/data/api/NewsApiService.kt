package com.example.newsmobileapplication.data.api

import com.example.newsmobileapplication.BuildConfig
import com.example.newsmobileapplication.model.entities.ArticleSearchResponse
import com.example.newsmobileapplication.model.entities.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApiService {

    // Fetches top stories from the specified section
    @GET("svc/topstories/v2/{section}.json")
    suspend fun getTopStories(
        @Path("section") section: String, // News section
        @Query("api-key") apiKey: String = BuildConfig.NEWS_API_KEY
    ): Response<NewsResponse>

    // Searches articles based on the provided query
    @GET("svc/search/v2/articlesearch.json")
    suspend fun searchArticles(
        @Query("q") query: String, // Search term
        @Query("api-key") apiKey: String =  BuildConfig.NEWS_API_KEY
    ): Response<ArticleSearchResponse>
}
