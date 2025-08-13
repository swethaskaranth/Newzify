package com.kaizencoder.newzify.presentation.headlines

import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.usecases.GetHeadlinesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HeadlinesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getHeadlinesUseCase = mockk<GetHeadlinesUseCase>()
    private lateinit var headlinesViewModel: HeadlinesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        headlinesViewModel = HeadlinesViewModel(getHeadlinesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun headlinesViewModel_getHeadlines_callsUseCaseOnce() {
        every { getHeadlinesUseCase.execute() } returns mockk()
        headlinesViewModel.getHeadlines()
        verify(exactly = 1) { getHeadlinesUseCase.execute() }
    }

    @Test
    fun headlinesViewModel_getHeadlines_updatesStateWithLoading() = runTest {
        every { getHeadlinesUseCase.execute() } returns flowOf(GetHeadlinesUseCase.GetHeadlinesUseCaseResult.Loading)
        headlinesViewModel.getHeadlines()
        advanceUntilIdle()
        assertTrue(headlinesViewModel.uiState.value.isLoading)
    }

    @Test
    fun headlinesViewModel_getHeadlines_updatesStateWithArticles() = runTest {
        val articles = listOf(
            Article("ID1","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate"),
            Article("ID2","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate"),
            Article("ID3","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate")
        )

        every { getHeadlinesUseCase.execute() } returns flowOf(
                    GetHeadlinesUseCase.GetHeadlinesUseCaseResult.Success(articles)
                )

        headlinesViewModel.getHeadlines()
        advanceUntilIdle()

        assertEquals(articles, headlinesViewModel.uiState.value.articles)
    }

    @Test
    fun headlinesViewModel_getHeadlines_updateStateWithError() = runTest {
        every { getHeadlinesUseCase.execute() } returns flowOf(
            GetHeadlinesUseCase.GetHeadlinesUseCaseResult.Error(
                "Something is not right. Please try again."
            )
        )

        headlinesViewModel.getHeadlines()
        advanceUntilIdle()

        assertEquals("Something is not right. Please try again.", headlinesViewModel.uiState.value.errorMessage)
    }

}
