package com.example.newsmobileapplication.data.api

import com.example.newsmobileapplication.BuildConfig
import com.example.newsmobileapplication.model.entities.ArticleSearchResponse
import com.example.newsmobileapplication.model.entities.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApiService {


    @GET("svc/topstories/v2/{section}.json")
    suspend fun getTopStories(
        @Path("section") section: String,
        @Query("api-key") apiKey: String = BuildConfig.NEWS_API_KEY
    ): Response<NewsResponse>


    // Article Search API için metod ekliyoruz
    @GET("svc/search/v2/articlesearch.json")
    suspend fun searchArticles(
        @Query("q") query: String, // Arama kelimesi
        @Query("api-key") apiKey: String =  BuildConfig.NEWS_API_KEY
    ): Response<ArticleSearchResponse> // Arama sonuçları için özel bir response sınıfı
}