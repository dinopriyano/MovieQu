package com.dupat.newsqu.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class ArticleEntity(

    val author: String?,

    val content: String?,

    val description: String?,

    val publishedAt: String?,

    @PrimaryKey(autoGenerate = false)
    val title: String,

    val url: String?,

    val urlToImage: String?
)