package com.kaizencoder.newzify.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kaizencoder.newzify.domain.model.Article

@Composable
fun ArticleItem(
    article: Article,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit
) {
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

            ArticleInfo(
                article,
                onSaveClick,
                onShareClick
            )
        }
    }
}
