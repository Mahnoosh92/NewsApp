package com.mahdavi.newsapp.data.repository

import com.mahdavi.newsapp.data.dataSource.local.LocalDataSource
import com.mahdavi.newsapp.data.dataSource.local.LocalDataSourceImpl
import com.mahdavi.newsapp.data.dataSource.remote.RemoteDataSource
import com.mahdavi.newsapp.data.dataSource.remote.RemoteDataSourceImpl
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.ArticleResponse
import com.mahdavi.newsapp.utils.extensions.getApiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*


class NewsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl.factory(),
    private val localDataSource: LocalDataSource = LocalDataSourceImpl()
) : NewsRepository {

    override suspend fun getArticles(topic: String): Flow<ResultWrapper<Exception, List<ArticleResponse?>>?> =
        flow {
            try {
                // TODO clear database
                updateLocalDataSource(topic)
                localDataSource.getArticles().flowOn(Dispatchers.IO).onEach { articles ->
                    emit(ResultWrapper.build {
                        articles.map {
                            it.toArticleResponse()
                        }
                    })
                }.collect()
            } catch (e: Exception) {
                emit(ResultWrapper.build {
                    throw e
                })
            }
        }

    private suspend fun updateLocalDataSource(topic: String) {
        try {
            remoteDataSource.getNews(topic).flowOn(Dispatchers.IO).onEach { response ->
                if (response.isSuccessful) {
                    response.body()?.articles?.let { list ->
                        localDataSource.insertAll(list.filterNotNull().map { it.toArticle() })
                            .flowOn(Dispatchers.IO).onEach {
                                // TODO: add to timber
                            }.collect()
                    }
                } else {
                    throw Exception(response.getApiError()?.message)
                }
            }.collect()
        } catch (e: Exception) {
            throw e
        }
    }
}

