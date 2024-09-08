package com.example.newsmobileapplication.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


// Date formatting function
fun formatDateTime(dateTime: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(dateTime)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        "Unknown date"
    }
}