package com.example.newsmobileapplication.utils

import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDateTime(dateTime: String): String {
    val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
    val targetFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    return try {
        val date = originalFormat.parse(dateTime)
        targetFormat.format(date)
    } catch (e: Exception) {
        dateTime
    }
}