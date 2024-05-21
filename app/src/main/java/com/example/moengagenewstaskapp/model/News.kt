package com.example.moengagenewstaskapp.model

data class NewsSource(
    val id: String?,
    val name: String
)
data class News(
    val source: NewsSource,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)