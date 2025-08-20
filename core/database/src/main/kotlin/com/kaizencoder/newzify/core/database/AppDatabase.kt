package com.kaizencoder.newzify.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kaizencoder.newzify.core.database.entity.Article

@Database(entities = [Article::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun articleDao(): ArticleDao
}
