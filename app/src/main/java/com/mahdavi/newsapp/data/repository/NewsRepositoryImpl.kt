package com.mahdavi.newsapp.data.repository

import com.mahdavi.newsapp.data.dataSource.local.LocalDataSource
import com.mahdavi.newsapp.data.dataSource.remote.RemoteDataSource
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.ArticleResponse
import com.mahdavi.newsapp.di.IoDispatcher
import com.mahdavi.newsapp.utils.extensions.getApiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NewsRepository {

    override suspend fun getArticles(topic: String): Flow<ResultWrapper<Exception, List<ArticleResponse?>>?> =
        flow {
            getNews(topic)
            localDataSource.getArticles()
                .flowOn(ioDispatcher)
                .onEach { articles ->
                    emit(ResultWrapper.build {
                        articles.map {
                            it.toArticleResponse()
                        }
                    })
                }
                .onCompletion { Timber.i("result is received") }
                .catch { error ->
                    Timber.e(error)
                    emit(ResultWrapper.build {
                        throw error
                    })
                }
                .collect()
        }

    private suspend fun getNews(topic: String) {
        remoteDataSource.getNews(topic)
            .flowOn(ioDispatcher)
            .onEach { response ->
                if (response.isSuccessful) {
                    response.body()?.articles?.let { list ->
                        // TODO: pagination must not get cleaned
                        clearDatabase()
                        updateDatabase(list)
                    }
                } else {
                    throw Exception(response.getApiError()?.message)
                }
            }
            .catch { error ->
                Timber.e(error)
                throw error
            }
            .collect()
    }

    private suspend fun updateDatabase(list: List<ArticleResponse?>) {
        localDataSource.update(list.filterNotNull().map { it.toArticle() })
            .flowOn(ioDispatcher)
            .onCompletion {
                Timber.i("database updated")
            }
            .catch { error ->
                Timber.e(error)
                throw error
            }
            .collect()
    }

    private suspend fun clearDatabase() {
        localDataSource.clear()
            .flowOn(ioDispatcher)
            .catch { error ->
                Timber.e(error)
                throw error
            }
            .collect()
    }
}

