package com.kaizencoder.newzify.presentation.common

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import com.kaizencoder.newzify.R

@Composable
fun ArticleThumbnail(thumbnail: String) {
    AsyncImage(
        model = thumbnail,
        contentDescription = stringResource(
            R.string.article_thumbnail_content_description
        ),
        contentScale = ContentScale.Companion.Crop,
        alignment = Alignment.Companion.TopCenter,
        modifier = Modifier.Companion
            .fillMaxWidth()
            .aspectRatio(ratio = 16f / 9f)

    )
}
