package com.kaizencoder.newzify.data.repository

import androidx.sqlite.SQLiteException
import com.kaizencoder.newzify.Constants
import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.data.local.ArticleDao
import com.kaizencoder.newzify.data.networking.NewsApiService
import com.kaizencoder.newzify.data.networking.dto.ArticleDto
import com.kaizencoder.newzify.data.networking.dto.ArticleFields
import com.kaizencoder.newzify.data.networking.dto.HeadlinesResponse
import com.kaizencoder.newzify.data.networking.dto.ResponseDto
import com.kaizencoder.newzify.domain.model.Article
import com.squareup.moshi.JsonDataException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.kaizencoder.newzify.data.local.entity.Article as ArticleEntity

class HeadlinesRepositoryTest {

    private val newsApiService = mockk<NewsApiService>()
    private val articleDao = mockk<ArticleDao>()
    private val headlinesRepository = HeadlinesRepositoryImpl(newsApiService, articleDao)
    val timeToLive = Constants.TIME_TO_LIVE

    @Test
    fun headlinesRepository_getHeadlines_emitsCachedHeadlines() = runTest {
        coEvery { articleDao.getHeadlines() } returns articleEntities
        val result = headlinesRepository.getHeadlines().first()

        assertTrue(result is DataResult.Success)
        assertEquals<List<Article>>(articles, result.data)

    }

    @Test
    fun headlinesRepository_getHeadlines_callsApiWhenCacheEmpty() = runTest {
        coEvery { newsApiService.getLatestHeadlines(any()) } returns mockk()
        every { articleDao.getHeadlines() } returns emptyList()
        headlinesRepository.getHeadlines().collect {

        }

        coVerify(exactly = 1) { newsApiService.getLatestHeadlines(any()) }
    }

    @Test
    fun headlinesRepository_getHeadlines_returnsCacheErrorOnSQLiteException() = runTest {
        every { articleDao.getHeadlines() } throws SQLiteException()
        val result = headlinesRepository.getHeadlines().first()

        assertEquals(DataResult.CacheError, result)
    }

    @Test
    fun headlinesRepository_getHeadlines_returnsCacheErrorOnIllegalStateException() = runTest {
        every { articleDao.getHeadlines() } throws IllegalStateException()
        val result = headlinesRepository.getHeadlines().first()

        assertEquals(DataResult.CacheError, result)
    }

    @Test
    fun headlinesRepository_getHeadlines_callsApiWhenTTLGreaterThan15Min() = runTest {
        every { articleDao.getHeadlines() } returns articleEntities
            .map {
                it.copy(
                    savedAt = System.currentTimeMillis() - timeToLive - 1
                )
            }
        coEvery { newsApiService.getLatestHeadlines(any()) } returns mockk()

        headlinesRepository.getHeadlines().last()
        coVerify(exactly = 1) { newsApiService.getLatestHeadlines(any()) }
    }

    @Test
    fun headlinesRepository_getHeadlines_doesNotCallApiWhenTTLLessThan15Min() = runTest {
        every { articleDao.getHeadlines() } returns articleEntities

        headlinesRepository.getHeadlines().last()
        coVerify(exactly = 0) { newsApiService.getLatestHeadlines(any()) }
    }

    @Test
    fun headLinesRepository_getHeadlines_clearsExistingDataWhenAPIReturnsSuccess() = runTest {
        every { articleDao.getHeadlines() } returns
                articleEntities
                    .map {
                        it.copy(
                            savedAt = System.currentTimeMillis() - timeToLive - 1
                        )
                    } andThen freshArticleEntities

        coEvery { articleDao.insert(any()) } returns Unit
        coEvery { newsApiService.getLatestHeadlines(any()) } returns headlinesResponse

        headlinesRepository.getHeadlines().last()
        coVerify(exactly = 1) { articleDao.clearCachedArticles() }
    }

    @Test
    fun headlinesRepository_getHeadlines_returnsFreshHeadlinesWhenCacheWriteSuccessful() = runTest {
        every { articleDao.getHeadlines() } returns
            articleEntities
                .map {
                    it.copy(
                        savedAt = System.currentTimeMillis() - timeToLive - 1
                    )
                } andThen freshArticleEntities

        coEvery { articleDao.insert(any()) } returns Unit
        coEvery { newsApiService.getLatestHeadlines(any()) } returns headlinesResponse

        val result = headlinesRepository.getHeadlines().last()

        verify(exactly = 2) { articleDao.getHeadlines() }
        assertTrue(result is DataResult.Success)
        assertEquals(freshArticles, result.data)
    }

    @Test
    fun headlinesRepository_getHeadlines_returns() = runTest {
        every { articleDao.getHeadlines() } returns
                articleEntities
                    .map {
                        it.copy(
                            savedAt = System.currentTimeMillis() - timeToLive - 1
                        )
                    }

        coEvery { newsApiService.getLatestHeadlines(any()) } returns
                headlinesResponse
                    .copy(
                        headlinesResponse.response.copy(
                            results = emptyList()
                        ))

        val result = headlinesRepository.getHeadlines().last()

        verify(exactly = 1) { articleDao.getHeadlines() }
        assertTrue(result is DataResult.Success)
        assertTrue(result.data.isEmpty())
    }

    @Test
    fun headlinesRepository_getHeadlines_cachesHeadlinesReceivedFromAPI() = runTest {
        every { articleDao.getHeadlines() } returnsMany listOf(
            articleEntities
                .map {
                    it.copy(
                        savedAt = System.currentTimeMillis() - timeToLive - 1
                    )
                },
            freshArticleEntities
        )
        coEvery { articleDao.insert(any()) } returns Unit
        coEvery { newsApiService.getLatestHeadlines(any()) } returns headlinesResponse

        headlinesRepository.getHeadlines().last()

        coVerify {
            articleDao.insert(
                match { insertedList ->
                    insertedList.size == 3
                            &&
                            insertedList.zip(freshArticleEntities).all {
                                (inserted, fresh) ->
                                inserted.articleId == fresh.articleId
                            }
                }
            )
        }
    }

    @Test
    fun headlinesRepository_getHeadlines_returnsCacheErrorOnCacheWriteFailure() = runTest {
        every { articleDao.getHeadlines() } returns articleEntities
                .map {
                    it.copy(
                        savedAt = System.currentTimeMillis() - timeToLive - 1
                    )
                }

        coEvery { articleDao.insert(any()) } throws SQLiteException()
        coEvery { newsApiService.getLatestHeadlines(any()) } returns headlinesResponse

        val result = headlinesRepository.getHeadlines().last()

        assertTrue(result is DataResult.CacheError)

    }

    @Test
    fun headlinesRepository_getHeadlines_returnsNetworkErrorOnHttpException() = runTest {
        every { articleDao.getHeadlines() } returns articleEntities
            .map {
                it.copy(
                    savedAt = System.currentTimeMillis() - timeToLive - 1
                )
            }

        coEvery { newsApiService.getLatestHeadlines(any()) } throws
                HttpException(
                    Response.error<String>(
                        404,
                        """{"error": "Not Found"}"""
                            .toResponseBody(
                                "application/json".toMediaTypeOrNull())
                    ))

        val result = headlinesRepository.getHeadlines().last()

        assertTrue(result is DataResult.NetworkError)

    }

    @Test
    fun headlinesRepository_getHeadlines_returnsNetworkErrorOnIOException() = runTest {
        every { articleDao.getHeadlines() } returns articleEntities
            .map {
                it.copy(
                    savedAt = System.currentTimeMillis() - timeToLive - 1
                )
            }

        coEvery { newsApiService.getLatestHeadlines(any()) } throws
                IOException()

        val result = headlinesRepository.getHeadlines().last()

        assertTrue(result is DataResult.NetworkError)

    }

    @Test
    fun headlinesRepository_getHeadlines_returnsNetworkErrorOnJsonDataException() = runTest {
        every { articleDao.getHeadlines() } returns articleEntities
            .map {
                it.copy(
                    savedAt = System.currentTimeMillis() - timeToLive - 1
                )
            }

        coEvery { newsApiService.getLatestHeadlines(any()) } throws
                JsonDataException("Invalid JSON")

        val result = headlinesRepository.getHeadlines().last()

        assertTrue(result is DataResult.NetworkError)

    }



    val articleEntities = listOf(
        ArticleEntity(
            articleId = "ID1",
            headline = "Headline",
            byline = "Byline",
            thumbnail = "Thumbnail",
            shortUrl = "ShortUrl",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            webPublicationDate = "PublishedDate",
            savedAt = System.currentTimeMillis()
        ),
        ArticleEntity(
            articleId = "ID2",
            headline = "Headline",
            byline = "Byline",
            thumbnail = "Thumbnail",
            shortUrl = "ShortUrl",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            webPublicationDate = "PublishedDate",
            savedAt = System.currentTimeMillis()
        ),
        ArticleEntity(
            articleId = "ID3",
            headline = "Headline",
            byline = "Byline",
            thumbnail = "Thumbnail",
            shortUrl = "ShortUrl",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            webPublicationDate = "PublishedDate",
            savedAt = System.currentTimeMillis()
        ),

        )

    val articles = listOf<Article>(
        Article(
            articleId = "ID1",
            headline = "Headline",
            byline = "Byline",
            thumbnail = "Thumbnail",
            shortUrl = "ShortUrl",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            timeSincePublished = "PublishedDate"
        ),
        Article(
            articleId = "ID2",
            headline = "Headline",
            byline = "Byline",
            thumbnail = "Thumbnail",
            shortUrl = "ShortUrl",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            timeSincePublished = "PublishedDate"
        ),
        Article(
            articleId = "ID3",
            headline = "Headline",
            byline = "Byline",
            thumbnail = "Thumbnail",
            shortUrl = "ShortUrl",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            timeSincePublished = "PublishedDate"
        ),
    )

    val newArticlesFromAPI = listOf(
        ArticleDto(
            id = "ID4",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            webPublicationDate = "PublishedDate",
            apiUrl = "apiUrl",
            isHosted = true,
            pillarId = "PillarId",
            pillarName = "PillarName",
            sectionId = "SectionId",
            sectionName = "SectionName",
            type = "Type",
            fields = ArticleFields(
                headline = "Headline",
                byline = "Byline",
                thumbnail = "Thumbnail",
                shortUrl = "ShortUrl",
            )
        ),
        ArticleDto(
            id = "ID5",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            webPublicationDate = "PublishedDate",
            apiUrl = "apiUrl",
            isHosted = true,
            pillarId = "PillarId",
            pillarName = "PillarName",
            sectionId = "SectionId",
            sectionName = "SectionName",
            type = "Type",
            fields = ArticleFields(
                headline = "Headline",
                byline = "Byline",
                thumbnail = "Thumbnail",
                shortUrl = "ShortUrl",
            )
        ),
        ArticleDto(
            id = "ID6",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            webPublicationDate = "PublishedDate",
            apiUrl = "apiUrl",
            isHosted = true,
            pillarId = "PillarId",
            pillarName = "PillarName",
            sectionId = "SectionId",
            sectionName = "SectionName",
            type = "Type",
            fields = ArticleFields(
                headline = "Headline",
                byline = "Byline",
                thumbnail = "Thumbnail",
                shortUrl = "ShortUrl",
            )
        )
    )

    val headlinesResponse = HeadlinesResponse(
        response = ResponseDto(
            currentPage = 1,
            orderBy = "",
            pages = 10,
            pageSize = 10,
            results = newArticlesFromAPI,
            startIndex = 1,
            total = 100,
            status = "OK",
            userTier = "userTier"
        )
    )

    val freshArticleEntities = listOf(
        ArticleEntity(
            articleId = "ID4",
            headline = "Headline",
            byline = "Byline",
            thumbnail = "Thumbnail",
            shortUrl = "ShortUrl",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            webPublicationDate = "PublishedDate",
            savedAt = System.currentTimeMillis()
        ),
        ArticleEntity(
            articleId = "ID5",
            headline = "Headline",
            byline = "Byline",
            thumbnail = "Thumbnail",
            shortUrl = "ShortUrl",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            webPublicationDate = "PublishedDate",
            savedAt = System.currentTimeMillis()
        ),
        ArticleEntity(
            articleId = "ID6",
            headline = "Headline",
            byline = "Byline",
            thumbnail = "Thumbnail",
            shortUrl = "ShortUrl",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            webPublicationDate = "PublishedDate",
            savedAt = System.currentTimeMillis()
        ),

        )

    val freshArticles = listOf<Article>(
        Article(
            articleId = "ID4",
            headline = "Headline",
            byline = "Byline",
            thumbnail = "Thumbnail",
            shortUrl = "ShortUrl",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            timeSincePublished = "PublishedDate"
        ),
        Article(
            articleId = "ID5",
            headline = "Headline",
            byline = "Byline",
            thumbnail = "Thumbnail",
            shortUrl = "ShortUrl",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            timeSincePublished = "PublishedDate"
        ),
        Article(
            articleId = "ID6",
            headline = "Headline",
            byline = "Byline",
            thumbnail = "Thumbnail",
            shortUrl = "ShortUrl",
            webTitle = "WebTitle",
            webUrl = "WebUrl",
            timeSincePublished = "PublishedDate"
        ),
    )

}
