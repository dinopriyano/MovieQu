package com.dupat.newsqu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.dupat.newsqu.data.repositories.NewsRepository
import com.dupat.newsqu.ui.model.Article
import com.dupat.newsqu.utils.toArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(val repository: NewsRepository) : ViewModel() {

    //for paging without offline caching (Remote mediator)
    val popularNewsPaging : Flow<PagingData<Article>> = repository.getResultPager().cachedIn(viewModelScope)

    @ExperimentalPagingApi
    val popularNews: Flow<PagingData<Article>> = repository.getResult().map {
        it.map { entity ->
            entity.toArticle()
        }
    }.cachedIn(viewModelScope)

    @ExperimentalPagingApi
    fun getPopNews() = popularNews

}