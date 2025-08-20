package com.kaizencoder.newzify.core.network.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HeadlinesResponse(
    @field:Json(name = "response")
    val response: ResponseDto
)
