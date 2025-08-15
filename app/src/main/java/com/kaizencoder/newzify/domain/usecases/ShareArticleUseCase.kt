package com.kaizencoder.newzify.domain.usecases

import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.model.ShareContent

class ShareArticleUseCase {

    fun execute(article: Article): ShareContent{
        return ShareContent(
            article.webTitle,
            article.webUrl
        )
    }
}
