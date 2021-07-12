package com.dupat.newsqu.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dupat.newsqu.data.local.entities.ArticleEntity
import com.dupat.newsqu.data.local.room.dao.NewsDao
import com.dupat.newsqu.data.local.room.dao.RemoteDao

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun remoteDao(): RemoteDao
}