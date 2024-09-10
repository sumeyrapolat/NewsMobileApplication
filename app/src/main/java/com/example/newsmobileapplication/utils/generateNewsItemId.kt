package com.example.newsmobileapplication.utils

import android.util.Log
import java.util.UUID

fun generateNewsItemId(newsUrl: String): String {
    val id = UUID.nameUUIDFromBytes(newsUrl.toByteArray()).toString()
    return id
}
