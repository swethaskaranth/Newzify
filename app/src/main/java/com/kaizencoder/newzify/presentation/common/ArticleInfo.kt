package com.kaizencoder.newzify.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kaizencoder.newzify.R
import com.kaizencoder.newzify.domain.model.Article

@Composable
fun ArticleInfo(
    article: Article,
    onShareClick: () -> Unit
) {
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
                contentDescription = stringResource(R.string.save_icon_content_description),
                onClick = onShareClick
            )
            ArticleAction(
                id = R.drawable.icon_share,
                contentDescription = stringResource(R.string.share_icon_content_description)
            ) {

            }
        }

    }
}
