package com.dupat.newsqu.ui.paging.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.dupat.newsqu.data.local.entities.ArticleEntity
import com.dupat.newsqu.data.local.entities.RemoteKeyEntity
import com.dupat.newsqu.data.local.room.dao.NewsDao
import com.dupat.newsqu.data.local.room.dao.RemoteDao
import com.dupat.newsqu.data.remote.network.ApiService
import com.dupat.newsqu.utils.Constants
import com.dupat.newsqu.utils.toArticleEntities
import java.io.InvalidObjectException

@ExperimentalPagingApi
class NewsRemoteMediator(private val newsDao: NewsDao, private val remoteDao: RemoteDao, private val apiService: ApiService): RemoteMediator<Int, ArticleEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, ArticleEntity>): MediatorResult {
        return try {
            val page = getKeyPageData(loadType, state) as Int
            val response = apiService.getPopularNews(Constants.API_KEY, Constants.COUNTRY, page, Constants.PAGE_SIZE)
            val endOfPagination = response.articles.size < Constants.PAGE_SIZE

            response.let {
                if(loadType == LoadType.REFRESH){
                    remoteDao.deleteByQuery()
                    newsDao.deleteAllNews()
                }

                val nextKeys = if(endOfPagination) null else page+1
                val prevKeys = if(page == 1) null else page-1

                val list = it.articles.map {
                    RemoteKeyEntity(it.title ,prevKeys, nextKeys)
                }

                remoteDao.insertOrReplace(list)
                newsDao.insertAllNews(it.articles.toArticleEntities())
            }

            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        }
        catch (e: Exception){
            MediatorResult.Error(e)
        }
    }

    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, ArticleEntity>): Any {
        return when(loadType){
            LoadType.APPEND -> {
                val remoteKey = getLastRemoteKey(state) ?: throw InvalidObjectException("Invalid Object")
                remoteKey.nextKey ?: MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.PREPEND -> {
                MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.REFRESH -> {
                val remoteKey = getClosestRemoteKeys(state)
                remoteKey?.nextKey?.minus(1) ?: 1
            }
        }
    }

    suspend fun getClosestRemoteKeys(state: PagingState<Int, ArticleEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let { article ->
                remoteDao.remoteKeyByQuery(article.title)
            }
        }
    }

    suspend fun getLastRemoteKey(state: PagingState<Int, ArticleEntity>): RemoteKeyEntity? {
        return state.lastItemOrNull()?.let { article ->
            remoteDao.remoteKeyByQuery(article.title)
        }
    }
}