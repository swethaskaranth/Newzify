package com.kaizencoder.newzify.domain.repository

import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface HeadlinesRepository {

    fun getHeadlines(): Flow<DataResult<List<Article>>>

}
