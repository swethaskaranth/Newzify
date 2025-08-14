package com.kaizencoder.newzify

import app.cash.turbine.test
import com.kaizencoder.newzify.data.DataResult
import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.repository.HeadlinesRepository
import com.kaizencoder.newzify.domain.usecases.GetHeadlinesUseCase
import com.kaizencoder.newzify.presentation.headlines.HeadlinesViewModel
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HeadlinesIntegrationTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockHeadlinesRepository : HeadlinesRepository = mockk()
    private val getHeadlinesUseCase : GetHeadlinesUseCase =
        GetHeadlinesUseCase(mockHeadlinesRepository)
    private val headlinesViewModel: HeadlinesViewModel = HeadlinesViewModel(getHeadlinesUseCase)

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun headlinesIntegration_getHeadlines_showLoadingThenArticles() = runTest {

        val articles = listOf(
            Article(
                "ID1",
                "Headline",
                "Byline",
                "Thumbnail",
                "ShortUrl",
                "WebUrl",
                "WebTitle",
                "PublishedDate"
            ),
            Article(
                "ID2",
                "Headline",
                "Byline",
                "Thumbnail",
                "ShortUrl",
                "WebUrl",
                "WebTitle",
                "PublishedDate"
            ),
            Article(
                "ID3",
                "Headline",
                "Byline",
                "Thumbnail",
                "ShortUrl",
                "WebUrl",
                "WebTitle",
                "PublishedDate"
            )
        )

        every { mockHeadlinesRepository.getHeadlines() } returns flowOf(
            DataResult.Success<List<Article>>(
                articles
            )
        )

        headlinesViewModel.uiState.test {
            headlinesViewModel.getHeadlines()

            skipItems(1)
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val successState = awaitItem()
            assertEquals(articles, successState.articles)
            assertFalse(successState.isLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun headlinesIntegration_getHeadlines_showLoadingTheError() = runTest {

        every { mockHeadlinesRepository.getHeadlines() } returns flowOf(
            DataResult.NetworkError
        )

        headlinesViewModel.uiState.test {
            headlinesViewModel.getHeadlines()

            skipItems(1)
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val errorState = awaitItem()
            assertEquals("Network Error", errorState.errorMessage)
            assertFalse(errorState.isLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
