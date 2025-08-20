package com.kaizencoder.newzify.core.domain.repository

import com.kaizencoder.newzify.core.common.DataResult
import com.kaizencoder.newzify.core.domain.model.Article

interface SavedArticlesRepository{

    suspend fun saveArticle(article: Article): DataResult<Unit>

    suspend fun getSavedArticles(): DataResult<List<Article>>
}
