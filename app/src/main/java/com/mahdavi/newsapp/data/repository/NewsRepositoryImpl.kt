package com.mahdavi.newsapp.data.repository

import com.mahdavi.newsapp.data.dataSource.remote.RemoteDataSource
import com.mahdavi.newsapp.data.dataSource.remote.RemoteDataSourceImpl
import com.mahdavi.newsapp.data.model.remote.News
import com.mahdavi.newsapp.data.model.local.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.mahdavi.newsapp.data.model.remote.Error
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import java.io.IOException


class NewsRepositoryImpl(private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl.factory()) :
    NewsRepository {

    override suspend fun getNews(
        topic: String
    ): Result<Exception, Pair<News?, Error?>>? {
        return try {
            var result: Result<Exception, Pair<News?, Error?>>? = null
            remoteDataSource.getNews(topic)
                .retry(2) { throwable ->
                    throwable is IOException
                }.flowOn(Dispatchers.IO)
                .catch { e ->
                    result = Result.build {
                        throw e
                    }
                }.collect {
                    result = if (it.isSuccessful) {
                        Result.build {
                            it.body() to null
                        }
                    } else {
                        Result.build {
                            null to null
                        }
                    }
                }
            result
        } catch (exception: Exception) {
            Result.build {
                throw exception
            }
        }
    }
}
