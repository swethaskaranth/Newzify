package com.kaizencoder.newzify.presentation.headlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaizencoder.newzify.core.domain.model.Article
import com.kaizencoder.newzify.core.domain.model.ShareContent
import com.kaizencoder.newzify.core.domain.usecases.GetHeadlinesUseCase
import com.kaizencoder.newzify.core.domain.usecases.SaveArticlesUseCase
import com.kaizencoder.newzify.core.domain.usecases.ShareArticleUseCase
import com.kaizencoder.newzify.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeadlinesViewModel @Inject constructor(
    private val getHeadlinesUseCase: GetHeadlinesUseCase,
    private val saveHeadlinesUseCase: SaveArticlesUseCase,
    private val shareArticleUseCase: ShareArticleUseCase): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _savedEvent = MutableSharedFlow<String>(replay = 1)
    val savedEvent: SharedFlow<String> = _savedEvent.asSharedFlow()

    fun getHeadlines(){
            getHeadlinesUseCase.execute()
                .onEach { useCaseResult ->
                    when(useCaseResult){
                        is GetHeadlinesUseCase.GetHeadlinesUseCaseResult.Loading -> {
                            _uiState.value = _uiState.value.copy(isLoading = true)
                        }
                        is GetHeadlinesUseCase.GetHeadlinesUseCaseResult.Success -> {
                            _uiState.value = UiState(articles = useCaseResult.articles)
                        }
                        is GetHeadlinesUseCase.GetHeadlinesUseCaseResult.Error -> {
                            _uiState.value = UiState(errorMessage = useCaseResult.message)
                        }
                    }
                }
                .launchIn(viewModelScope)
    }

    fun saveHeadlines(article: Article){
        viewModelScope.launch {
            val result = saveHeadlinesUseCase.execute(article)
            when(result){
                is SaveArticlesUseCase.SaveHeadlinesUseCaseResult.Success -> {
                    _savedEvent.emit("Article Saved")
                }
                is SaveArticlesUseCase.SaveHeadlinesUseCaseResult.Error -> {
                    _savedEvent.emit(result.message)
                }
            }
        }
    }

    fun shareArticle(article: Article): ShareContent {
        return shareArticleUseCase.execute(article)
    }


}
