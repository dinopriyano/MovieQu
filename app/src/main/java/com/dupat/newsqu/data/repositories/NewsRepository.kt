package com.dupat.newsqu.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dupat.newsqu.data.local.entities.ArticleEntity
import com.dupat.newsqu.data.local.room.NewsDatabase
import com.dupat.newsqu.data.remote.network.ApiService
import com.dupat.newsqu.ui.model.Article
import com.dupat.newsqu.ui.paging.mediator.NewsRemoteMediator
import com.dupat.newsqu.ui.paging.source.NewsPagingSource
import com.dupat.newsqu.utils.Constants
import kotlinx.coroutines.flow.Flow

class NewsRepository(private val apiService: ApiService, private val newsDatabase: NewsDatabase) {

    fun getSearchResult(query: String) : Flow<PagingData<Article>>{
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, maxSize = 100 ,enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(apiService, query) }
        ).flow
    }

    @ExperimentalPagingApi
    fun getResult(): Flow<PagingData<ArticleEntity>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = NewsRemoteMediator(
                database = newsDatabase,
                networkService = apiService
            )
        ){
            newsDatabase.newsDao().getAllNews()
        }.flow
    }

}