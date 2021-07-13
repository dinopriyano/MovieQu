package com.dupat.newsqu.utils

import androidx.recyclerview.widget.DiffUtil
import com.dupat.newsqu.ui.model.Article

object DiffUtils: DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.title == newItem.title
    }
}