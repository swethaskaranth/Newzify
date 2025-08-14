package com.kaizencoder.newzify.data.networking.dto


import com.kaizencoder.newzify.data.local.entity.Article
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleDto(
    @field:Json(name = "apiUrl")
    val apiUrl: String,
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "isHosted")
    val isHosted: Boolean,
    @field:Json(name = "pillarId")
    val pillarId: String,
    @field:Json(name = "pillarName")
    val pillarName: String,
    @field:Json(name = "sectionId")
    val sectionId: String,
    @field:Json(name = "sectionName")
    val sectionName: String,
    @field:Json(name = "type")
    val type: String,
    @field:Json(name = "webPublicationDate")
    val webPublicationDate: String,
    @field:Json(name = "webTitle")
    val webTitle: String,
    @field:Json(name = "webUrl")
    val webUrl: String,
    @field:Json(name = "headline")
    val headline: String,
    @field:Json(name = "byline")
    val byline: String,
    @field:Json(name = "thumbnail")
    val thumbnail: String,
    @field:Json(name = "shortUrl")
    val shortUrl: String
)

fun ArticleDto.toArticleEntity(savedByUser: Boolean = false) = Article(
    articleId = this.id,
    headline = this.headline,
    byline = this.byline,
    thumbnail = this.thumbnail,
    shortUrl = this.shortUrl,
    webUrl = this.webUrl,
    webTitle = this.webTitle,
    webPublicationDate = this.webPublicationDate,
    savedByUser = savedByUser
)
