package com.dupat.newsqu.data.repositories

import androidx.paging.*
import com.dupat.newsqu.data.local.entities.ArticleEntity
import com.dupat.newsqu.data.local.room.NewsDatabase
import com.dupat.newsqu.data.local.room.dao.NewsDao
import com.dupat.newsqu.data.local.room.dao.RemoteDao
import com.dupat.newsqu.data.remote.network.ApiService
import com.dupat.newsqu.ui.model.Article
import com.dupat.newsqu.ui.paging.mediator.NewsRemoteMediator
import com.dupat.newsqu.ui.paging.source.NewsPagingSource
import com.dupat.newsqu.utils.Constants
import kotlinx.coroutines.flow.Flow

class NewsRepository(private val apiService: ApiService, private val newsDatabase: NewsDatabase) {

    //for paging without offline caching (remote mediator)
    fun getResultPager() : Flow<PagingData<Article>>{
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, prefetchDistance = 5, enablePlaceholders = false),
            pagingSourceFactory = {NewsPagingSource(apiService)}
        ).flow
    }

    @ExperimentalPagingApi
    fun getResult() : Flow<PagingData<ArticleEntity>>{
        val pagingSourceFactory = {newsDatabase.newsDao().getAllNews()}
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 5, enablePlaceholders = true),
            remoteMediator = NewsRemoteMediator(database = newsDatabase, networkService = apiService),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}