package com.kaizencoder.newzify.presentation.savedArticles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaizencoder.newzify.presentation.common.ArticleItem
import com.kaizencoder.newzify.presentation.common.UiState
import timber.log.Timber

@Composable
fun SavedArticlesScreen(
    modifier: Modifier = Modifier,
    viewModel: SavedArticlesViewModel = hiltViewModel()
) {

    val uiState: UiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(true) {
        Timber.i("Launched effect called")
        viewModel.getSavedArticles()
    }

    when {
        uiState.isLoading -> {
            Column(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        !uiState.errorMessage.isNullOrEmpty() -> {
            Column(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiState.errorMessage,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }

        else -> {
            LazyColumn(
                modifier.fillMaxSize()
            ) {
                items(uiState.articles) { article ->

                    ArticleItem(article, { }, {})
                }
            }
        }


    }
}
