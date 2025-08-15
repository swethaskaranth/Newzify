package com.kaizencoder.newzify.presentation.savedArticles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaizencoder.newzify.domain.usecases.GetSavedArticlesUseCase
import com.kaizencoder.newzify.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedArticlesViewModel @Inject constructor(
    private val getSavedArticlesUseCase: GetSavedArticlesUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun getSavedArticles(){
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
           val useCaseResult = getSavedArticlesUseCase.execute()
            when(useCaseResult){
                is GetSavedArticlesUseCase.GetSavedArticlesUseCaseResult.Success -> {
                    _uiState.value = UiState(articles = useCaseResult.articles)
                }
                is GetSavedArticlesUseCase.GetSavedArticlesUseCaseResult.Error -> {
                    _uiState.value = UiState(errorMessage = useCaseResult.message)
                }
            }
        }
    }

}
