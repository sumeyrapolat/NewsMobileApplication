package com.example.newsmobileapplication.utils

fun generateArticleId(url: String): String {
    return url.hashCode().toString()
}
