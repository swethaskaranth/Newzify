package com.kaizencoder.newzify.domain.usecases

import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.repository.SavedArticlesRepository
import com.kaizencoder.newzify.domain.usecases.SaveHeadlinesUseCase.SaveHeadlinesUseCaseResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test

class SaveHeadlinesUseCaseTest {

    private val savedArticlesRepository: SavedArticlesRepository = mockk()
    private val saveHeadlinesUseCase = SaveHeadlinesUseCase(savedArticlesRepository)

    //saveHeadlines execute method calls the repository method with the same parameter
    @Test
    fun saveHeadlines_callsRepositoryMethod() {
        val article = Article("ID1","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate")
        every { savedArticlesRepository.saveArticle(article) } returns DataResult.Success(Unit)
        saveHeadlinesUseCase.execute( article )

        verify { savedArticlesRepository.saveArticle(article) }
    }

    //saveHeadlines execute method returns success when repository call is successful
    @Test
    fun saveHeadlines_returnsSuccess_whenRepositoryCallIsSuccessful() {
        val article = Article("ID1","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate")
        every { savedArticlesRepository.saveArticle(article) } returns DataResult.Success(Unit)
        val result = saveHeadlinesUseCase.execute( article )

        assertTrue(result is SaveHeadlinesUseCaseResult.Success)
    }

    //saveHeadlines execute method returns error when repository call fails
    @Test
    fun saveHeadlines_returnsError_whenRepositoryCallFailsWithCacheError() {
        val article = Article("ID1","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate")
        every { savedArticlesRepository.saveArticle(article) } returns DataResult.CacheError
        val result = saveHeadlinesUseCase.execute( article )

        assertTrue(result is SaveHeadlinesUseCaseResult.Error)
        assertEquals("Cache Error", (result as SaveHeadlinesUseCaseResult.Error).message)
    }

    @Test
    fun saveHeadlines_returnsError_whenRepositoryCallFailsWithUnknownError() {
        val article = Article("ID1","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate")
        every { savedArticlesRepository.saveArticle(article) } returns DataResult.UnknownError
        val result = saveHeadlinesUseCase.execute( article )

        assertTrue(result is SaveHeadlinesUseCaseResult.Error)
        assertEquals("Something is not right. Please try again.", (result as SaveHeadlinesUseCaseResult.Error).message)
    }

}