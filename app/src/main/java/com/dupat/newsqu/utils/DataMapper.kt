package com.dupat.newsqu.utils

import com.dupat.newsqu.data.local.entities.ArticleEntity
import com.dupat.newsqu.ui.model.Article

fun List<Article>.toArticleEntities() : List<ArticleEntity>{
    val entities = ArrayList<ArticleEntity>()
    this.map {
        val entity = ArticleEntity(
            it.author,
            it.content,
            it.description,
            it.publishedAt,
            it.title,
            it.url,
            it.urlToImage
        )

        entities.add(entity)
    }

    return entities
}