package com.kaizencoder.newzify.domain.model

data class Article(
    val articleId: String,
    val headline: String,
    val byline: String,
    val thumbnail: String,
    val shortUrl: String,
    val webUrl: String,
    val webTitle: String,
    val timeSincePublished: String
)
