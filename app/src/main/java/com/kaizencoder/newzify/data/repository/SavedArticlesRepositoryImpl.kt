package com.kaizencoder.newzify.data.repository

import androidx.sqlite.SQLiteException
import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.data.local.ArticleDao
import com.kaizencoder.newzify.data.local.entity.toArticleEntity
import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.repository.SavedArticlesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SavedArticlesRepositoryImpl @Inject constructor(
    private val articleDao: ArticleDao
) : SavedArticlesRepository {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun saveArticle(article: Article): DataResult<Unit> {
         return withContext(Dispatchers.IO) {
            try {
                articleDao.saveArticle(article.toArticleEntity(true))
                DataResult.Success(Unit)
            }catch (ex: SQLiteException){
                Timber.e(ex,"Error saving article")
                DataResult.CacheError
            }catch(ex: Exception){
                Timber.e(ex,"Error saving article")
                DataResult.GenericError(ex.message ?: "Something is not right. Please try again.")
            }
        }
    }
}
