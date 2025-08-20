package com.kaizencoder.newzify.core.data.repository

import androidx.sqlite.SQLiteException
import com.kaizencoder.newzify.core.common.DataResult
import com.kaizencoder.newzify.core.domain.model.Article
import com.kaizencoder.newzify.core.domain.repository.Logger
import com.kaizencoder.newzify.core.domain.repository.SavedArticlesRepository
import com.kaizencoder.newzify.core.database.ArticleDao
import com.kaizencoder.newzify.core.database.entity.toArticleDomain
import com.kaizencoder.newzify.core.database.entity.toArticleEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SavedArticlesRepositoryImpl @Inject constructor(
    private val articleDao: ArticleDao,
    private val logger: Logger
) : SavedArticlesRepository {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun saveArticle(article: Article): DataResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                articleDao.saveArticle(article.toArticleEntity(true))
                DataResult.Success(Unit)
            } catch (ex: SQLiteException) {
                logger.e(ex, "Error saving article")
                DataResult.CacheError
            } catch (ex: Exception) {
                logger.e(ex, "Error saving article")
                DataResult.GenericError(ex.message ?: "Something is not right. Please try again.")
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getSavedArticles(): DataResult<List<Article>> {
        return withContext(Dispatchers.IO) {
            try {
                val articles = articleDao.getSavedArticles()
                DataResult.Success(articles.map { it.toArticleDomain() })
            } catch (ex: SQLiteException) {
                logger.e(ex, "Error getting saved articles")
                DataResult.CacheError
            } catch (ex: Exception) {
                logger.e(ex, "Error saving article")
                DataResult.GenericError(ex.message ?: "Something is not right. Please try again.")
            }

        }

    }
}
