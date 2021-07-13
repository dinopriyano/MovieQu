package com.dupat.newsqu.data.remote.response

import com.dupat.newsqu.ui.model.Article

data class ApiResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)