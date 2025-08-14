package com.kaizencoder.newzify.presentation.headlines

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.kaizencoder.newzify.R
import com.kaizencoder.newzify.domain.model.Article

@Composable
fun HeadlinesScreen(
    modifier: Modifier = Modifier,
    headlinesViewModel: HeadlinesViewModel = viewModel()
) {

    val uiState = headlinesViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        headlinesViewModel.getHeadlines()
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
                    ArticleItem(article)
                }
            }
        }
    }


}

@Composable
fun ArticleItem(article: Article) {
    Card(
        shape = RectangleShape
    ) {
        Column {

            ArticleThumbnail(article.thumbnail)

            Text(
                text = article.headline,
                modifier = Modifier
                    .padding(
                        vertical = 12.dp,
                        horizontal = 16.dp
                    ),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            ArticleInfo(article)
        }
    }
}

@Composable
private fun ArticleInfo(article: Article) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 4.dp,
                horizontal = 16.dp
            )
    ) {
        Text(
            text = article.timeSincePublished
        )

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            ArticleAction(
                id = R.drawable.icon_save,
                contentDescription = stringResource(R.string.save_icon_content_description))
            ArticleAction(
                id = R.drawable.icon_share,
                contentDescription = stringResource(R.string.share_icon_content_description))
        }

    }
}

@Composable
fun ArticleAction(@DrawableRes id: Int,
                  contentDescription: String) {
    Icon(
        painter = painterResource(id),
        contentDescription = contentDescription,
        modifier = Modifier
            .padding(all = 8.dp)
            .size(24.dp)
    )
}
@Composable
fun ArticleThumbnail(thumbnail: String) {
    AsyncImage(
        model = thumbnail,
        contentDescription = stringResource(
            R.string.article_thumbnail_content_description
        ),
        contentScale = ContentScale.Crop,
        alignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio = 16f / 9f)

    )
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
        )
    )
}
