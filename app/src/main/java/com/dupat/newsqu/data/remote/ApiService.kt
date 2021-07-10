package com.dupat.newsqu.data.remote

import com.dupat.newsqu.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int
    )

}