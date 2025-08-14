package com.kaizencoder.newzify.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaizencoder.newzify.data.local.entity.Article

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article")
    fun getHeadlines(): List<Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articles: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article: Article)
}
