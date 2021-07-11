package com.dupat.newsqu.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dupat.newsqu.data.remote.network.ApiService
import com.dupat.newsqu.ui.model.Article
import com.dupat.newsqu.ui.paging.source.NewsPagingSource
import kotlinx.coroutines.flow.Flow

class NewsRepository(private val apiService: ApiService) {

    fun getResult() : Flow<PagingData<Article>>{
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {NewsPagingSource(apiService)}
        ).flow
    }

}