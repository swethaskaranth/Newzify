package com.kaizencoder.newzify.domain.usecases

import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.repository.HeadlinesRepository
import com.kaizencoder.newzify.domain.usecases.GetHeadlinesUseCase.GetHeadlinesUseCaseResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Test


class GetHeadlinesUseCaseTest {

    private val headlinesRepositoryMock = mockk<HeadlinesRepository>()
    private val getHeadLinesUseCase = GetHeadlinesUseCase(headlinesRepositoryMock)

    @Test
    fun `getHeadlines should call repository method only once`() = runTest{
        every { headlinesRepositoryMock.getHeadlines() } returns flowOf(DataResult.Success<List<Article>>(emptyList()))
        getHeadLinesUseCase.execute().collect()
        verify(exactly = 1) { headlinesRepositoryMock.getHeadlines() }
    }

    @Test
    fun `getHeadlines should emit a loading state`() = runTest {
        every { headlinesRepositoryMock.getHeadlines() } returns flowOf(
            DataResult.Success<List<Article>>(
                emptyList()
            )
        )
        val result = getHeadLinesUseCase.execute().first()
        assert(result is GetHeadlinesUseCaseResult.Loading)
    }

    @Test
    fun `getHeadlines should emit a list of articles when repository call is successful`() = runTest {
        every { headlinesRepositoryMock.getHeadlines() } returns flowOf(
            DataResult.Success<List<Article>>(
                listOf(Article("ID1","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate"),
                    Article("ID2","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate"),
                    Article("ID3","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate"))
            )
        )

        val result = getHeadLinesUseCase.execute().last()
        assert(result is GetHeadlinesUseCaseResult.Success)
        assert((result as GetHeadlinesUseCaseResult.Success).articles.size == 3)
    }

    @Test
    fun `getHeadlines should emit an error state when repository call fails due to network error`() = runTest {
        every { headlinesRepositoryMock.getHeadlines() } returns flowOf(DataResult.NetworkError)
        val result = getHeadLinesUseCase.execute().last()
        assert(result is GetHeadlinesUseCaseResult.Error)
        assert((result as GetHeadlinesUseCaseResult.Error).message == "Network Error")

    }

    @Test
    fun `getHeadlines should emit an error state when repository call fails due to cache error`() = runTest {
        every { headlinesRepositoryMock.getHeadlines() } returns flowOf(DataResult.CacheError)
        val result = getHeadLinesUseCase.execute().last()
        assert(result is GetHeadlinesUseCaseResult.Error)
        assert((result as GetHeadlinesUseCaseResult.Error).message == "Cache Error")

    }

    @Test
    fun `getHeadlines should emit an error state when repository call fails due to general error`() = runTest {
        every { headlinesRepositoryMock.getHeadlines() } returns flowOf(DataResult.GenericError("Mapping error"))
        val result = getHeadLinesUseCase.execute().last()
        assert(result is GetHeadlinesUseCaseResult.Error)
        assert((result as GetHeadlinesUseCaseResult.Error).message == "Mapping error")
    }

    @Test
    fun `getHeadlines should emit an error state when repository call fails due to unknown error`() = runTest {
        every { headlinesRepositoryMock.getHeadlines() } returns flowOf(DataResult.UnknownError)
        val result = getHeadLinesUseCase.execute().last()
        assert(result is GetHeadlinesUseCaseResult.Error)
        assert((result as GetHeadlinesUseCaseResult.Error).message == "Something is not right. Please try again.")
    }

    @Test
    fun `getHeadlines should emit an error state when repository throws an exception`() = runTest {
        every { headlinesRepositoryMock.getHeadlines() } throws RuntimeException()
        val result = getHeadLinesUseCase.execute().last()
        assert(result is GetHeadlinesUseCaseResult.Error)
    }

}
