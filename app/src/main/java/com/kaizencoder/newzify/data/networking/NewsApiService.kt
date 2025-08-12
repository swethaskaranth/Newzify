package com.kaizencoder.newzify.data.networking

import com.kaizencoder.newzify.BuildConfig
import com.kaizencoder.newzify.data.networking.dto.HeadlinesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("search")
    suspend fun getLatestHeadlines(
        @Query("api-key") apiKey: String = BuildConfig.API_KEY,
        @Query("query-fields") queryFields: String = "headline,byline,thumbnail,short-url",) : HeadlinesResponse
}
