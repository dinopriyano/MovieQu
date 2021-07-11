package com.dupat.newsqu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dupat.newsqu.data.repositories.NewsRepository
import com.dupat.newsqu.ui.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository): ViewModel() {

    val popularNews : Flow<PagingData<Article>> = newsRepository.getResult().cachedIn(viewModelScope)

    fun getPopNews() = popularNews

}