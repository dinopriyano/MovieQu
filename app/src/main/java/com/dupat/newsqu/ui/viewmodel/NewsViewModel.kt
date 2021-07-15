package com.dupat.newsqu.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.dupat.newsqu.data.repositories.NewsRepository
import com.dupat.newsqu.ui.model.Article
import com.dupat.newsqu.utils.Constants
import com.dupat.newsqu.utils.toArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(val repository: NewsRepository) : ViewModel() {


    val currentQuery = MutableStateFlow(Constants.DEFAULT_QUERY)

    fun searchNews(query: String){
        currentQuery.value = query
    }

    @ExperimentalCoroutinesApi
    val newsSearchResult: LiveData<PagingData<Article>> = currentQuery.flatMapLatest { query ->
        repository.getSearchResult(query).cachedIn(viewModelScope)
    }.asLiveData()

    @ExperimentalCoroutinesApi
    fun getSearchNews() = newsSearchResult


    @ExperimentalPagingApi
    val newsPopularResult: Flow<PagingData<Article>> = repository.getResult().map {
        it.map { entity ->
            entity.toArticle()
        }
    }.cachedIn(viewModelScope)

    @ExperimentalPagingApi
    fun getPopularNews() = newsPopularResult

}