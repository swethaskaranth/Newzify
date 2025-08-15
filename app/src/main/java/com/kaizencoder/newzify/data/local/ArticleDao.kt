package com.kaizencoder.newzify.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kaizencoder.newzify.data.local.entity.Article

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article where saved_by_user = 0")
    fun getHeadlines(): List<Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articles: List<Article>)

    @Update
    suspend fun saveArticle(article: Article)

    @Query("DELETE FROM article where saved_by_user = 0")
    suspend fun clearCachedArticles()

    @Query("SELECT * FROM article where saved_by_user = 1")
    fun getSavedArticles(): List<Article>
}
