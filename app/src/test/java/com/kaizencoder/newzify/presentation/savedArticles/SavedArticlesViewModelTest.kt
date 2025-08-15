package com.kaizencoder.newzify.presentation.savedArticles

import app.cash.turbine.test
import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.usecases.GetSavedArticlesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SavedArticlesViewModelTest {

    private val getSavedArticlesUseCase = mockk<GetSavedArticlesUseCase>()
    private val viewModel = SavedArticlesViewModel(getSavedArticlesUseCase)

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    private val articles = listOf(
        Article("ID1","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate"),
        Article("ID2","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate"),
        Article("ID3","Headline","Byline", "Thumbnail", "ShortUrl","WebUrl","WebTitle","PublishedDate")
    )

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun savedArticlesViewModel_getSavedArticles_callsUseCase() = runTest {
        coEvery { getSavedArticlesUseCase.execute() } returns
                GetSavedArticlesUseCase.GetSavedArticlesUseCaseResult.Success(
                    emptyList()
                )
        viewModel.getSavedArticles()
        advanceUntilIdle()
        coVerify (exactly = 1) { getSavedArticlesUseCase.execute() }
    }

    @Test
    fun savedArticlesViewModel_getSavedArticles_updatesStateToLoading() = runTest {
        coEvery { getSavedArticlesUseCase.execute() } returns
                GetSavedArticlesUseCase.GetSavedArticlesUseCaseResult.Success(
                    emptyList()
                )
        viewModel.uiState.test {
            viewModel.getSavedArticles()
            skipItems(1)
            val result = awaitItem()
            assertEquals(true, result.isLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun savedArticlesViewModel_getSavedArticles_updatesStateToSuccess() = runTest {
        coEvery { getSavedArticlesUseCase.execute() } returns
                GetSavedArticlesUseCase.GetSavedArticlesUseCaseResult.Success(
            articles
        )

        viewModel.getSavedArticles()

        advanceUntilIdle()

        assertEquals(articles, viewModel.uiState.value.articles)

    }

    @Test
    fun savedArticlesViewModel_getSavedArticles_updatesStateToError() = runTest {
        coEvery { getSavedArticlesUseCase.execute() } returns
                GetSavedArticlesUseCase.GetSavedArticlesUseCaseResult.Error(
                    "Cache Error"
                )

        viewModel.getSavedArticles()

        advanceUntilIdle()

        assertEquals("Cache Error", viewModel.uiState.value.errorMessage)

    }
}
