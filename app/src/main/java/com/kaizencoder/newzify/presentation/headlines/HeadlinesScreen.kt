package com.kaizencoder.newzify.presentation.headlines

import androidx.browser.customtabs.CustomTabsIntent
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.presentation.common.ArticleItem
import androidx.core.net.toUri

@Composable
fun HeadlinesScreen(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    headlinesViewModel: HeadlinesViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val uiState = headlinesViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        headlinesViewModel.getHeadlines()
        headlinesViewModel.savedEvent.collect { message ->
            snackBarHostState.showSnackbar(message)
        }
    }

    when {
        uiState.value.isLoading -> Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }

        !uiState.value.errorMessage.isNullOrEmpty() -> {
            Column(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiState.value.errorMessage ?: "Something went wrong. Please try again.",
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

        }

        else -> {
            LazyColumn(
                modifier.fillMaxSize()
            ) {
                items(uiState.value.articles) { article ->
                    ArticleItem(
                        article,
                        onSaveClick = {
                            headlinesViewModel.saveHeadlines(article)
                        },
                        onShareClick = {
                            val shareContent = headlinesViewModel.shareArticle(article)
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, shareContent.title)
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "${shareContent.title}\n${shareContent.url}"
                                )
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        },
                        onCardClick = {
                            val customTabsIntent = CustomTabsIntent.Builder()
                                .setShowTitle(true)
                                .build()
                            customTabsIntent.launchUrl(context, article.webUrl.toUri())
                        })
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun ArticleItemPreview() {
    ArticleItem(
        Article(
            "ID1",
            "Distracting the public’: group of health professionals call for RFK Jr to be removed",
            "Byline",
            thumbnail = "https://media.guim.co.uk/b1c4e91860d940333e0908c0bf26120e30a97bd6/301_0_3333_2667/500.jpg",
            "ShortUrl",
            "WebUrl",
            "WebTitle",
            "1d"
        ), {}, {}, {})
}
