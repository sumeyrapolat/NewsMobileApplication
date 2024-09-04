package com.example.newsmobileapplication.data.api

import com.example.newsmobileapplication.model.entities.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/everything")
    suspend fun getNews(
        @Query("domains") domains: String = "bbc.com,cnn.com",
        @Query("from") from: String,       // Son 24 saat için haberler
        @Query("pageSize") pageSize: Int = 50,  // 50 haber çekmek için
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = "ce8ab07efffa440aa8807379dd5042ca"
    ): Response<NewsResponse>
}
