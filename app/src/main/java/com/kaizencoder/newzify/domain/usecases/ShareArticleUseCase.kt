package com.kaizencoder.newzify.domain.usecases

import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.model.ShareContent
import javax.inject.Inject

class ShareArticleUseCase @Inject constructor() {

    fun execute(article: Article): ShareContent{
        return ShareContent(
            article.webTitle,
            article.webUrl
        )
    }
}
