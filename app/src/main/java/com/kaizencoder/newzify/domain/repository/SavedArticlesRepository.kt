package com.kaizencoder.newzify.domain.repository

import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.domain.model.Article

interface SavedArticlesRepository{

    suspend fun saveArticle(article: Article): DataResult<Unit>

    suspend fun getSavedArticles(): DataResult<List<Article>>
}
