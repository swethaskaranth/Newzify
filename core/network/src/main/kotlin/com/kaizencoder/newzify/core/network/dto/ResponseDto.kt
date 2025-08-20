package com.kaizencoder.newzify.core.network.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseDto(
    @field:Json(name = "currentPage")
    val currentPage: Int,
    @field:Json(name = "orderBy")
    val orderBy: String,
    @field:Json(name = "pageSize")
    val pageSize: Int,
    @field:Json(name = "pages")
    val pages: Int,
    @field:Json(name = "results")
    val results: List<ArticleDto>,
    @field:Json(name = "startIndex")
    val startIndex: Int,
    @field:Json(name = "status")
    val status: String,
    @field:Json(name = "total")
    val total: Int,
    @field:Json(name = "userTier")
    val userTier: String
)
