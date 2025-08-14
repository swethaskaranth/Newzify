package com.kaizencoder.newzify.domain.repository

import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.domain.model.Article

interface SavedArticlesRepository{

    fun saveArticle(article: Article): DataResult<Unit>
}
