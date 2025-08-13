package com.kaizencoder.newzify.data.repository

import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.repository.HeadlinesRepository
import kotlinx.coroutines.flow.Flow

class HeadlinesRepositoryImpl: HeadlinesRepository {

    override fun getHeadlines(): Flow<DataResult<List<Article>>> {
        TODO("Not yet implemented")
    }
}
