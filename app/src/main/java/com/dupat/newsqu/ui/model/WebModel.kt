package com.dupat.newsqu.ui.model

data class WebModel(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)