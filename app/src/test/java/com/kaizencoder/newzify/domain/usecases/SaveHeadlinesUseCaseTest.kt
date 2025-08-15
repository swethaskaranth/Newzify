package com.kaizencoder.newzify.domain.usecases

import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.repository.SavedArticlesRepository
import com.kaizencoder.newzify.domain.usecases.SaveArticlesUseCase.SaveHeadlinesUseCaseResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SaveHeadlinesUseCaseTest {

    private val savedArticlesRepository: SavedArticlesRepository = mockk()
    private val saveHeadlinesUseCase = SaveArticlesUseCase(savedArticlesRepository)

    @Test
    fun saveHeadlines_callsRepositoryMethod() = runTest {
        val article = Article("ID1","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate")
        coEvery { savedArticlesRepository.saveArticle(article) } returns DataResult.Success(Unit)
        saveHeadlinesUseCase.execute( article )

        coVerify { savedArticlesRepository.saveArticle(article) }
    }

    @Test
    fun saveHeadlines_returnsSuccess_whenRepositoryCallIsSuccessful() = runTest {
        val article = Article("ID1","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate")
        coEvery { savedArticlesRepository.saveArticle(article) } returns DataResult.Success(Unit)
        val result = saveHeadlinesUseCase.execute( article )

        assertTrue(result is SaveHeadlinesUseCaseResult.Success)
    }

    @Test
    fun saveHeadlines_returnsError_whenRepositoryCallFailsWithCacheError() = runTest {
        val article = Article("ID1","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate")
        coEvery { savedArticlesRepository.saveArticle(article) } returns DataResult.CacheError
        val result = saveHeadlinesUseCase.execute( article )

        assertTrue(result is SaveHeadlinesUseCaseResult.Error)
        assertEquals("Cache Error", (result as SaveHeadlinesUseCaseResult.Error).message)
    }

    @Test
    fun saveHeadlines_returnsError_whenRepositoryCallFailsWithUnknownError() = runTest {
        val article = Article("ID1","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate")
        coEvery { savedArticlesRepository.saveArticle(article) } returns DataResult.UnknownError
        val result = saveHeadlinesUseCase.execute( article )

        assertTrue(result is SaveHeadlinesUseCaseResult.Error)
        assertEquals("Something is not right. Please try again.", (result as SaveHeadlinesUseCaseResult.Error).message)
    }

}
