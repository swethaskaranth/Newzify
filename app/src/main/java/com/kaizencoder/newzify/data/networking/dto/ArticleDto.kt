package com.kaizencoder.newzify.data.networking.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleDto(
    @Json(name = "apiUrl")
    val apiUrl: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "isHosted")
    val isHosted: Boolean,
    @Json(name = "pillarId")
    val pillarId: String,
    @Json(name = "pillarName")
    val pillarName: String,
    @Json(name = "sectionId")
    val sectionId: String,
    @Json(name = "sectionName")
    val sectionName: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "webPublicationDate")
    val webPublicationDate: String,
    @Json(name = "webTitle")
    val webTitle: String,
    @Json(name = "webUrl")
    val webUrl: String,
    @Json(name = "headline")
    val headline: String,
    @Json(name = "byline")
    val byline: String,
    @Json(name = "thumbnail")
    val thumbnail: String,
    @Json(name = "shortUrl")
    val shortUrl: String
)