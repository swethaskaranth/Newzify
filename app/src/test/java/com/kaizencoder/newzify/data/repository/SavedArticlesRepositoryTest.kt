package com.kaizencoder.newzify.data.repository

import androidx.sqlite.SQLiteException
import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.data.local.ArticleDao
import com.kaizencoder.newzify.data.local.entity.Article as ArticleEntity
import com.kaizencoder.newzify.domain.model.Article
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SavedArticlesRepositoryTest {

    private val articleDao = mockk<ArticleDao>()
    private val savedArticlesRepository = SavedArticlesRepositoryImpl(articleDao)
    val article = Article(
        "ID1",
        "Headline",
        "Byline",
        "Thumbnail",
        "ShortUrl",
        "WebUrl",
        "WebTitle",
        "PublishedDate")

    @Test
    fun saveArticleRepository_callsSaveArticles() = runTest {
        coEvery { articleDao.saveArticle(any()) } returns Unit
        savedArticlesRepository.saveArticle(article)
        coVerify { articleDao.saveArticle(match { article ->
            article.articleId == "ID1"
        }) }
    }

    @Test
    fun saveArticleRepository_returnsSuccess_whenSaveArticlesReturnsSuccess() = runTest {
        coEvery { articleDao.saveArticle(any()) } returns Unit
        val result = savedArticlesRepository.saveArticle(article)
        assertTrue(result is DataResult.Success)
    }

    @Test
    fun saveArticleRepository_returnsError_whenSaveArticlesThrowsException() = runTest {
        coEvery { articleDao.saveArticle(any()) } throws SQLiteException()
        val result = savedArticlesRepository.saveArticle(article)
        assertTrue(result is DataResult.CacheError)
    }

    @Test
    fun saveArticleRepository_returnsGenericErrorWithMessage_whenSaveArticlesThrowsException() = runTest {
        coEvery { articleDao.saveArticle(any()) } throws RuntimeException("Illegal argument")
        val result = savedArticlesRepository.saveArticle(article)
        assertTrue(result is DataResult.GenericError)
        assertEquals("Illegal argument", (result as DataResult.GenericError).message)
    }

    @Test
    fun saveArticleRepository_returnsGenericError_whenSaveArticlesThrowsException() = runTest {
        coEvery { articleDao.saveArticle(any()) } throws RuntimeException()
        val result = savedArticlesRepository.saveArticle(article)
        assertTrue(result is DataResult.GenericError)
        assertEquals("Something is not right. Please try again.", (result as DataResult.GenericError).message)
    }

    //getSavedArticles calls articleDao's getSavedArticles
    @Test
    fun getSavedArticlesRepository_getSavedArticles_callsGetSavedArticlesInDao() = runTest {
        coEvery { articleDao.getSavedArticles() } returns emptyList()
        savedArticlesRepository.getSavedArticles()

        coVerify(exactly = 1) {
            articleDao.getSavedArticles()
        }
    }

    //getSavedArticles returns success when articleDao returns success
    @Test
    fun getSavedArticlesRepository_getSavedArticles_returnsSuccess() = runTest {
        coEvery { articleDao.getSavedArticles() } returns articleEntities
        val result = savedArticlesRepository.getSavedArticles()

        assertTrue(result is DataResult.Success)
        assertEquals(articles, (result as DataResult.Success).data)
    }


    //getSavedArticles returns error when articleDao throws exception
    @Test
    fun getSavedArticlesRepository_getSavedArticles_returnsErrorWhenExceptionThrown() = runTest {
        coEvery { articleDao.getSavedArticles() } throws SQLiteException()
        val result = savedArticlesRepository.getSavedArticles()

        assertTrue(result is DataResult.CacheError)
    }

    @Test
    fun getSavedArticles_returnsGenericErrorWithMessage_whenRuntimeExceptionThrown() = runTest {
        coEvery { articleDao.getSavedArticles() } throws RuntimeException("Illegal argument")
        val result = savedArticlesRepository.getSavedArticles()

        assertTrue(result is DataResult.GenericError)
    }

    @Test
    fun getSavedArticlesRepository_getSavedArticles_returnsErrorWhenRuntimeExceptionThrown() = runTest {
        coEvery { articleDao.getSavedArticles() } throws RuntimeException()
        val result = savedArticlesRepository.getSavedArticles()

        assertTrue(result is DataResult.GenericError)
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
}
