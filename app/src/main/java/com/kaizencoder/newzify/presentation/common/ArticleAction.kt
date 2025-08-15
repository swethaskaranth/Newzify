package com.kaizencoder.newzify.presentation.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ArticleAction(
    @DrawableRes id: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(onClick) {
        Icon(
            painter = painterResource(id),
            contentDescription = contentDescription,
            modifier = Modifier.Companion
                .padding(all = 8.dp)
                .size(24.dp)
        )
    }

}
