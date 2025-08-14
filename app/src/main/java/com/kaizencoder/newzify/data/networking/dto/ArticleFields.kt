package com.kaizencoder.newzify.data.networking.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleFields(
    @field:Json(name = "byline")
    val byline: String?,
    @field:Json(name = "headline")
    val headline: String?,
    @field:Json(name = "shortUrl")
    val shortUrl: String?,
    @field:Json(name = "thumbnail")
    val thumbnail: String?
)
