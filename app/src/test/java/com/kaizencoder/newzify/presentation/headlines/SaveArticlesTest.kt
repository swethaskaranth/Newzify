package com.kaizencoder.newzify.presentation.headlines

import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.usecases.GetHeadlinesUseCase
import com.kaizencoder.newzify.domain.usecases.SaveArticlesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SaveArticlesTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getHeadlinesUseCase = mockk<GetHeadlinesUseCase>()
    private val savedArticlesUseCase = mockk<SaveArticlesUseCase>()
    private lateinit var headlinesViewModel: HeadlinesViewModel

    private val article = Article(
        "ID1",
        "Headline",
        "Byline",
        "Thumbnail",
        "ShortUrl",
        "WebUrl",
        "WebTitle",
        "PublishedDate")

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        headlinesViewModel = HeadlinesViewModel(
            getHeadlinesUseCase,
            savedArticlesUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun saveHeadlines_callsUseCaseExecute() = runTest{
        coEvery { savedArticlesUseCase.execute(any()) } returns
                SaveArticlesUseCase.SaveHeadlinesUseCaseResult.Success
        headlinesViewModel.saveHeadlines(article)
        advanceUntilIdle()
        coVerify { savedArticlesUseCase.execute(article) }
    }

    @Test
    fun saveHeadlines_updatesStateWithSuccess() = runTest {
        coEvery { savedArticlesUseCase.execute(any()) } returns
                SaveArticlesUseCase.SaveHeadlinesUseCaseResult.Success
       headlinesViewModel.saveHeadlines(article)
        advanceUntilIdle()

        val result = headlinesViewModel.savedEvent.first()
        assertTrue(result == "Article Saved")
    }

    @Test
    fun saveHeadlines_updatesStateWithError() = runTest {
        coEvery { savedArticlesUseCase.execute(any()) } returns
                SaveArticlesUseCase.SaveHeadlinesUseCaseResult.Error("Cache Error")
        headlinesViewModel.saveHeadlines(article)
        advanceUntilIdle()

        val result = headlinesViewModel.savedEvent.first()
        assertTrue(result == "Cache Error")
    }
}
