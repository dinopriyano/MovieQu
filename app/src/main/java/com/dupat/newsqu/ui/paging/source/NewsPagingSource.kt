package com.dupat.newsqu.ui.paging.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dupat.newsqu.data.remote.network.ApiService
import com.dupat.newsqu.ui.model.Article
import com.dupat.newsqu.utils.Constants
import retrofit2.HttpException
import java.io.IOException

class NewsPagingSource(val service: ApiService) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1)?:anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {

        return try{
            val page = params.key?:1
            val response = service.getPopularNews(Constants.API_KEY, Constants.COUNTRY, page, Constants.PAGE_SIZE)
            val data = response.articles
            LoadResult.Page(
                data = data,
                prevKey = if(page == 1) null else page-1,
                nextKey = if(data.isEmpty()) null else page+1
            )
        }
        catch (e: IOException){
            LoadResult.Error(e)
        }
        catch (e: HttpException){
            LoadResult.Error(e)
        }

    }
}