package com.kaizencoder.newzify.domain.usecases

import com.kaizencoder.newzify.domain.model.Article
import com.kaizencoder.newzify.domain.model.ShareContent
import org.junit.Test
import kotlin.test.assertEquals

class ShareArticleUseCaseTest {

    private val shareArticleUseCase = ShareArticleUseCase()

    private val article = Article(
        "ID1",
        "Headline",
        "Byline",
        "Thumbnail",
        "ShortUrl",
        "WebUrl",
        "WebTitle",
        "PublishedDate")

    @Test
    fun shareArticleUseCase_returnsSharingContent() {
        val result = shareArticleUseCase.execute(article)

        val expected = ShareContent(
            title = article.webTitle,
            url = article.webUrl
        )
        assertEquals(expected, result)
    }
}
