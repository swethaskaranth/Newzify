package com.kaizencoder.newzify.data.repository

import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.repository.SavedArticlesRepository

class SavedArticlesRepositoryImpl: SavedArticlesRepository {
    override fun saveArticle(article: Article): DataResult<Unit> {
        TODO("Not yet implemented")
    }
}