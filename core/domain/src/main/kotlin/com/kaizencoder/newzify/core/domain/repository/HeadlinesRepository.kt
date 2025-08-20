package com.kaizencoder.newzify.core.domain.repository

import com.kaizencoder.newzify.core.common.DataResult
import com.kaizencoder.newzify.core.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface HeadlinesRepository {

    fun getHeadlines(): Flow<DataResult<List<Article>>>

}
