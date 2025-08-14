package com.kaizencoder.newzify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kaizencoder.newzify.data.local.entity.Article

@Database(entities = [Article::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun articleDao(): ArticleDao
}