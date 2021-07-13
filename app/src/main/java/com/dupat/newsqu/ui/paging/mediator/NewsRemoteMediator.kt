package com.dupat.newsqu.ui.paging.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dupat.newsqu.data.local.entities.ArticleEntity
import com.dupat.newsqu.data.local.entities.RemoteKeyEntity
import com.dupat.newsqu.data.local.room.NewsDatabase
import com.dupat.newsqu.data.remote.network.ApiService
import com.dupat.newsqu.utils.Constants
import com.dupat.newsqu.utils.toArticleEntities
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val database: NewsDatabase,
    private val networkService: ApiService
) : RemoteMediator<Int, ArticleEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {

        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        return try {
            val response = networkService.getPopularNews(
                Constants.API_KEY,
                Constants.COUNTRY,
                page,
                state.config.pageSize
            )

            val isEndOfList = response.articles.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.newsDao().deleteAllNews()
                    database.remoteDao().deleteByQuery()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.articles.map {
                    RemoteKeyEntity(it.title, prevKey = prevKey, nextKey = nextKey)
                }

                database.remoteDao().insertOrReplace(keys)
                database.newsDao().insertAllNews(response.articles.toArticleEntities())
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ArticleEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.title?.let { title ->
                database.remoteDao().remoteKeyByQuery(title)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, ArticleEntity>): RemoteKeyEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { article -> database.remoteDao().remoteKeyByQuery(article.title) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, ArticleEntity>): RemoteKeyEntity? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { article -> database.remoteDao().remoteKeyByQuery(article.title) }
    }
}