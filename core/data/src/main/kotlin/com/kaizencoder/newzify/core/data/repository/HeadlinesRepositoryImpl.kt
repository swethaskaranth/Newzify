package com.kaizencoder.newzify.core.data.repository

import androidx.sqlite.SQLiteException
import com.kaizencoder.newzify.core.common.DataResult
import com.kaizencoder.newzify.core.database.ArticleDao
import com.kaizencoder.newzify.core.domain.model.Article
import com.kaizencoder.newzify.core.domain.repository.CachePolicy
import com.kaizencoder.newzify.core.domain.repository.HeadlinesRepository
import com.kaizencoder.newzify.core.domain.repository.Logger
import com.kaizencoder.newzify.core.network.NewsApiService
import com.kaizencoder.newzify.core.database.entity.toArticleDomain
import com.kaizencoder.newzify.core.network.dto.ResponseDto
import com.kaizencoder.newzify.core.network.dto.toArticleEntity
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import com.kaizencoder.newzify.core.database.entity.Article as ArticleEntity

class HeadlinesRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val articleDao: ArticleDao,
    private val cachePolicy: CachePolicy,
    private val logger: Logger
) :
    HeadlinesRepository {

    override fun getHeadlines(): Flow<DataResult<List<Article>>> = flow<DataResult<List<Article>>> {
        val articles = getArticlesFromDb()

        if (articles.isNotEmpty())
            emit(
                DataResult.Success(
                articles.map {
                    it.toArticleDomain()
                }
            ))

        if (articles.isEmpty() || cachePolicy.hasCacheExpired(articles[0].savedAt) )
            emit(fetchAndSaveArticles())


    }.catch { exception ->
        when (exception) {
            is IllegalStateException -> {
                logger.e(exception, "IllegalStateException in HeadlinesRepository")
                emit(DataResult.CacheError)
            }

            is SQLiteException -> {
                logger.e(exception, "SQLiteException in HeadlinesRepository")
                emit(DataResult.CacheError)
            }

            is HttpException -> {
                logger.e(exception, "HttpException in HeadlinesRepository")
                emit(DataResult.NetworkError)
            }

            is IOException -> {
                logger.e(exception, "IOException in HeadlinesRepository")
                emit(DataResult.NetworkError)
            }

            is JsonDataException -> {
                logger.e(exception, "HttpException in HeadlinesRepository")
                emit(DataResult.NetworkError)
            }
        }
    }
        .flowOn(Dispatchers.IO)

    private suspend fun fetchAndSaveArticles(): DataResult<List<Article>> {
        val response = newsApiService.getLatestHeadlines().response
        if (response.results.isNotEmpty()) {
            return insertArticles(response)
        } else
            return DataResult.Success(emptyList())
    }

    private suspend fun insertArticles(response: ResponseDto): DataResult.Success<List<Article>> {
        val articlesToInsert : List<ArticleEntity> = response.results.map { it.toArticleEntity() }
        replaceArticlesCache(articlesToInsert)
        return DataResult.Success(getArticlesFromDb().map { article -> article.toArticleDomain() })
    }

    private suspend fun replaceArticlesCache(articlesToInsert: List<ArticleEntity>){
        articleDao.clearCachedArticles()
        articleDao.insert(articlesToInsert)
    }

    private fun getArticlesFromDb(): List<ArticleEntity> {
        return articleDao.getHeadlines()
    }
}
