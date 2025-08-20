package com.kaizencoder.newzify.core.domain.usecases

import com.kaizencoder.newzify.core.common.DataResult
import com.kaizencoder.newzify.core.domain.model.Article
import com.kaizencoder.newzify.core.domain.repository.Logger
import com.kaizencoder.newzify.core.domain.repository.SavedArticlesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertTrue

class GetSavedArticlesUseCaseTest {

    private val savedArticlesRepository = mockk<SavedArticlesRepository>()
    private val loggerMock = mockk<Logger>()
    private val getSavedArticlesUseCase = GetSavedArticlesUseCase(
        savedArticlesRepository,
        loggerMock)

    private val articles = listOf(
        Article("ID1","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate"),
        Article("ID2","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate"),
        Article("ID3","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate")
    )

    @Test
    fun getSavedArticlesUseCase_callsRepositoryMethod() = runTest{
        coEvery { savedArticlesRepository.getSavedArticles() } returns DataResult.Success<List<Article>>(emptyList())
        getSavedArticlesUseCase.execute()

        coVerify (exactly = 1) { savedArticlesRepository.getSavedArticles() }

    }

    @Test
    fun getSavedArticlesUseCase_returnsSavedArticles() = runTest{
        coEvery { savedArticlesRepository.getSavedArticles() } returns DataResult.Success<List<Article>>(articles)
        val result = getSavedArticlesUseCase.execute()

        assertTrue(result is GetSavedArticlesUseCase.GetSavedArticlesUseCaseResult.Success)
        assertEquals(articles, result.articles)
    }

    @Test
    fun getSavedArticlesUseCase_returnsEmptyList_whenNoArticlesSaved() = runTest{
        coEvery { savedArticlesRepository.getSavedArticles() } returns DataResult.Success<List<Article>>(emptyList())
        val result = getSavedArticlesUseCase.execute()

        assertTrue(result is GetSavedArticlesUseCase.GetSavedArticlesUseCaseResult.Success)
        assertEquals(emptyList<List<Article>>(), result.articles)
    }

    @Test
    fun getSavedArticlesUseCase_returnsError_whenRepositoryReturnsCacheError() = runTest {
        coEvery { savedArticlesRepository.getSavedArticles() } returns DataResult.CacheError

        val result = getSavedArticlesUseCase.execute()

        assertTrue(result is GetSavedArticlesUseCase.GetSavedArticlesUseCaseResult.Error)
        assertEquals("Cache Error", result.message)
    }

    @Test
    fun getSavedArticlesUseCase_returnsError_whenRepositoryReturnsGenericError() = runTest {
        coEvery { savedArticlesRepository.getSavedArticles() } returns DataResult.GenericError("Mapping Error")

        val result = getSavedArticlesUseCase.execute()

        assertTrue(result is GetSavedArticlesUseCase.GetSavedArticlesUseCaseResult.Error)
        assertEquals("Mapping Error", result.message)
    }

    @Test
    fun getSavedArticlesUseCase_returnsError_whenRepositoryReturnsAnythingElse() = runTest {
        coEvery { savedArticlesRepository.getSavedArticles() } returns DataResult.UnknownError

        val result = getSavedArticlesUseCase.execute()

        assertTrue(result is GetSavedArticlesUseCase.GetSavedArticlesUseCaseResult.Error)
    }

    @Test
    fun getSavedArticlesUseCase_returnsError_whenRepositoryThrowsRuntimeException() = runTest {
        coEvery { savedArticlesRepository.getSavedArticles() } throws RuntimeException()

        val result = getSavedArticlesUseCase.execute()

        assertTrue(result is GetSavedArticlesUseCase.GetSavedArticlesUseCaseResult.Error)
        assertEquals("Something is not right. Please try again.", result.message)
    }

}
