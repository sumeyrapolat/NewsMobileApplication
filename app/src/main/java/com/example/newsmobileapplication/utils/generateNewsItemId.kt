package com.example.newsmobileapplication.utils

fun generateNewsItemId(title: String, publishedAt: String): String {
    return title.hashCode().toString() + publishedAt.hashCode().toString()
}

