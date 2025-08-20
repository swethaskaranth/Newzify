package com.kaizencoder.newzify.core.network

import com.kaizencoder.core.network.BuildConfig
import com.kaizencoder.newzify.core.network.dto.HeadlinesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("search")
    suspend fun getLatestHeadlines(
        @Query("api-key") apiKey: String = BuildConfig.API_KEY,
        @Query("show-fields") queryFields: String = "headline,byline,thumbnail,short-url",) : HeadlinesResponse
}