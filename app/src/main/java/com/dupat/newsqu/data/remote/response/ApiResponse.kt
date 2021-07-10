package com.dupat.newsqu.data.remote.response

import com.dupat.newsqu.ui.model.Article

data class ApiResponse<T> (
    val articles: List<T>,
    val status: String,
    val totalResults: Int
)