package com.dupat.newsqu.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dupat.newsqu.data.local.entities.ArticleEntity
import com.dupat.newsqu.data.local.entities.RemoteKeyEntity
import com.dupat.newsqu.data.local.room.dao.NewsDao
import com.dupat.newsqu.data.local.room.dao.RemoteDao
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [ArticleEntity::class, RemoteKeyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun remoteDao(): RemoteDao

    companion object{
        @Volatile
        private var INSTANCE : NewsDatabase? = null
        fun getInstance(ctx: Context) : NewsDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    //Encrypt database using sqlcipher
                    val passphrase = SQLiteDatabase.getBytes(
                        "dupat.id".toCharArray()
                    )
                    val factory = SupportFactory(passphrase)
                    instance = Room.databaseBuilder(
                        ctx.applicationContext,
                        NewsDatabase::class.java,
                        "news_db"
                    )
                        .fallbackToDestructiveMigration()
                        .openHelperFactory(factory)
                        .build()
                }

                return instance
            }
        }
    }
}