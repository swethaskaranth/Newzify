package com.kaizencoder.newzify.presentation.headlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaizencoder.newzify.domain.usecases.GetHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HeadlinesViewModel @Inject constructor(private val getHeadlinesUseCase: GetHeadlinesUseCase): ViewModel() {

    private val _uiState = MutableStateFlow(HeadlinesUiState())
    val uiState: StateFlow<HeadlinesUiState> = _uiState.asStateFlow()

    fun getHeadlines(){
            getHeadlinesUseCase.execute()
                .onEach { useCaseResult ->
                    when(useCaseResult){
                        is GetHeadlinesUseCase.GetHeadlinesUseCaseResult.Loading -> {
                            _uiState.value = _uiState.value.copy(isLoading = true)
                        }
                        is GetHeadlinesUseCase.GetHeadlinesUseCaseResult.Success -> {
                            _uiState.value = HeadlinesUiState(articles = useCaseResult.articles)
                        }
                        is GetHeadlinesUseCase.GetHeadlinesUseCaseResult.Error -> {
                            _uiState.value = HeadlinesUiState(errorMessage = useCaseResult.message)
                        }
                    }
                }
                .launchIn(viewModelScope)
    }

}
