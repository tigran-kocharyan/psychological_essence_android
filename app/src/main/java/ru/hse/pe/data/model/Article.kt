package ru.hse.pe.data.model

data class Article(
    val views: Int? = 0,
    val likes: Int? = 0,
    val title: String = "",
    val time: String = "",
    val imageUrl: String = "",
    val mdUrl: String = "",
    val category: String = ""
)