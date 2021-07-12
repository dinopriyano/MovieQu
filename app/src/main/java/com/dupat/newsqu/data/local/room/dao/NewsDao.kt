package com.dupat.newsqu.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.dupat.newsqu.data.local.entities.ArticleEntity

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNews(data: List<ArticleEntity>)

    @Query("DELETE FROM article")
    suspend fun deleteAllNews()

    @Query("SELECT * FROM article")
    fun getAllNews() : PagingSource<Int, ArticleEntity>

    @Query("SELECT * FROM article WHERE title LIKE '%' || :keyword || '%' OR description LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%' OR author LIKE '%' || :keyword || '%'")
    fun searchNews(keyword: String) : PagingSource<Int, ArticleEntity>

}