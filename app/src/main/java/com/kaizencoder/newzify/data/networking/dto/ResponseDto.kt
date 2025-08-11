package com.kaizencoder.newzify.data.networking.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseDto(
    @Json(name = "currentPage")
    val currentPage: Int,
    @Json(name = "orderBy")
    val orderBy: String,
    @Json(name = "pageSize")
    val pageSize: Int,
    @Json(name = "pages")
    val pages: Int,
    @Json(name = "results")
    val results: List<ArticleDto>,
    @Json(name = "startIndex")
    val startIndex: Int,
    @Json(name = "status")
    val status: String,
    @Json(name = "total")
    val total: Int,
    @Json(name = "userTier")
    val userTier: String
)