package com.example.newsmobileapplication.utils

import android.util.Log
import java.util.UUID

fun generateNewsItemId(newsUrl: String): String {
    val id = UUID.nameUUIDFromBytes(newsUrl.toByteArray()).toString()
    Log.d("generateNewsItemId", "Generated UUID for URL $newsUrl: $id")
    return id
}
