package com.example.newsmobileapplication.utils

// Fonksiyonu sadece String parametresi alacak şekilde güncelliyoruz
fun generateNewsItemId(url: String): String {
    return url.hashCode().toString()
}
