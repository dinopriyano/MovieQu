package com.dupat.newsqu.data.remote.network

import com.dupat.newsqu.data.remote.response.ApiResponse
import com.dupat.newsqu.ui.model.Article
import com.dupat.newsqu.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top-headlines")
    suspend fun getPopularMovies(
        @Query("apiKey") apiKey: String = Constants.API_KEY,
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ) : ApiResponse<Article>

    @GET("everything")
    suspend fun getSearchMovies(
        @Query("apiKey") apiKey: String = Constants.API_KEY,
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ) : ApiResponse<Article>

}