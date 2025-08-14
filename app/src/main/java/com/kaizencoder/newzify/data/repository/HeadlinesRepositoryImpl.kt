package com.kaizencoder.newzify.data.repository

import androidx.sqlite.SQLiteException
import com.kaizencoder.newzify.Constants
import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.data.local.ArticleDao
import com.kaizencoder.newzify.data.local.entity.toArticleDomain
import com.kaizencoder.newzify.data.networking.NewsApiService
import com.kaizencoder.newzify.data.networking.dto.toArticleEntity
import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.repository.HeadlinesRepository
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject
import com.kaizencoder.newzify.data.local.entity.Article as ArticleEntity

class HeadlinesRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val articleDao: ArticleDao
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

        if (articles.isEmpty() || articles[0].savedAt < System.currentTimeMillis() - Constants.TIME_TO_LIVE)
            emit(fetchAndSaveArticles())


    }.catch { exception ->
        when (exception) {
            is IllegalStateException -> {
                Timber.e(exception, "IllegalStateException in HeadlinesRepository")
                emit(DataResult.CacheError)
            }

            is SQLiteException -> {
                Timber.e(exception, "SQLiteException in HeadlinesRepository")
                emit(DataResult.CacheError)
            }

            is HttpException -> {
                Timber.e(exception, "HttpException in HeadlinesRepository")
                emit(DataResult.NetworkError)
            }

            is IOException -> {
                Timber.e(exception, "IOException in HeadlinesRepository")
                emit(DataResult.NetworkError)
            }

            is JsonDataException -> {
                Timber.e(exception, "HttpException in HeadlinesRepository")
                emit(DataResult.NetworkError)
            }
        }
    }
        .flowOn(Dispatchers.IO)

    private suspend fun fetchAndSaveArticles(): DataResult<List<Article>> {
        val response = newsApiService.getLatestHeadlines().response
        if (response.results.isNotEmpty()) {
            val articlesToInsert = response.results.map { it.toArticleEntity() }
            articleDao.insert(articlesToInsert)
            val articles = getArticlesFromDb().map { article -> article.toArticleDomain() }
            return DataResult.Success(articles)
        } else
            return DataResult.Success(emptyList())
    }

    private fun getArticlesFromDb(): List<ArticleEntity> {
        return articleDao.getHeadlines()
    }
}
