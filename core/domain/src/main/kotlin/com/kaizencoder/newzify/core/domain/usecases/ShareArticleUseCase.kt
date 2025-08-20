package com.kaizencoder.newzify.core.domain.usecases

import com.kaizencoder.newzify.core.domain.model.Article
import com.kaizencoder.newzify.core.domain.model.ShareContent

import javax.inject.Inject

class ShareArticleUseCase @Inject constructor() {

    fun execute(article: Article): ShareContent{
        return ShareContent(
            article.webTitle,
            article.webUrl
        )
    }
}
