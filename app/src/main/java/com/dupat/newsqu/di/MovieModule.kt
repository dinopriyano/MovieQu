package com.dupat.newsqu.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.dupat.newsqu.data.local.room.NewsDatabase
import com.dupat.newsqu.data.remote.network.ApiService
import com.dupat.newsqu.data.repositories.NewsRepository
import com.dupat.newsqu.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
            .client(OkHttpClient.Builder().also { client->
                val logger = HttpLoggingInterceptor()
                logger.level = HttpLoggingInterceptor.Level.BASIC
                client.addInterceptor(logger)
            }.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideNewsDatabase(@ApplicationContext appContext: Context): NewsDatabase{
        val passphrase = SQLiteDatabase.getBytes("dupatid".toCharArray())
        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(appContext, NewsDatabase::class.java, "news.db")
            .fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideNewsRepository(apiService: ApiService, newsDatabase: NewsDatabase) : NewsRepository{
        return NewsRepository(apiService, newsDatabase)
    }

}