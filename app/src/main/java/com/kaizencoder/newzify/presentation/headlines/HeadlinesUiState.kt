package com.kaizencoder.newzify.presentation.headlines

import com.kaizencoder.newzify.domain.model.Article

data class HeadlinesUiState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val errorMessage: String? = null
)
