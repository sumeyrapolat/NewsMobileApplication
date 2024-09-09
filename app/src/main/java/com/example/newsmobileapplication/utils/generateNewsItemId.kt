package com.example.newsmobileapplication.utils

import android.util.Log

fun generateNewsItemId(newsUrl: String): String {
    val id = newsUrl.hashCode().toString()
    Log.d("generateNewsItemId", "Generated ID for URL $newsUrl: $id")
    return id
}

