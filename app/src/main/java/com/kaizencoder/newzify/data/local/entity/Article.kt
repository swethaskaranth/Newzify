package com.kaizencoder.newzify.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Article(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "article_id") val articleId: String,
    @ColumnInfo(name = "headline") val headline: String,
    @ColumnInfo(name = "byline") val byline: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String,
    @ColumnInfo(name = "short_url") val shortUrl: String,
    @ColumnInfo(name = "web_title") val webTitle: String,
    @ColumnInfo(name = "web_url") val webUrl: String,
    @ColumnInfo(name = "web_publication_date") val webPublicationDate: String,
    @ColumnInfo(name = "saved_by_user") val savedByUser: Boolean = false,
    @ColumnInfo(name = "saved_at") val savedAt: Long = System.currentTimeMillis()
)

fun Article.toArticleDomain() =
    com.kaizencoder.newzify.domain.model.Article(
        articleId = this.articleId,
        headline = this.headline,
        byline = this.byline,
        thumbnail = this.thumbnail,
        shortUrl = this.shortUrl,
        webUrl = this.webUrl,
        webTitle = this.webTitle,
        timeSincePublished = this.webPublicationDate
    )

fun com.kaizencoder.newzify.domain.model.Article.toArticleEntity(savedByUser: Boolean = false) = Article(
    articleId = this.articleId,
    headline = this.headline,
    byline = this.byline,
    thumbnail = this.thumbnail,
    shortUrl = this.shortUrl,
    webUrl = this.webUrl,
    webTitle = this.webTitle,
    webPublicationDate = this.timeSincePublished,
    savedByUser = savedByUser
)
