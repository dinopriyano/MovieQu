package com.dupat.newsqu.di

import android.app.Application
import androidx.room.Room
import com.dupat.newsqu.data.local.room.NewsDatabase
import com.dupat.newsqu.data.remote.network.ApiService
import com.dupat.newsqu.data.repositories.NewsRepository
import com.dupat.newsqu.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance() : ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideNewsDatabase(app: Application): NewsDatabase{
        val passphrase = SQLiteDatabase.getBytes("dupatid".toCharArray())
        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(app, NewsDatabase::class.java, "news.db")
            .fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsRepository(apiService: ApiService, newsDatabase: NewsDatabase) : NewsRepository{
        return NewsRepository(apiService, newsDatabase.newsDao(), newsDatabase.remoteDao())
    }

}