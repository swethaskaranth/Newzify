package com.kaizencoder.newzify.presentation.common

import com.kaizencoder.newzify.domain.model.Article

data class UiState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val errorMessage: String? = null
)
