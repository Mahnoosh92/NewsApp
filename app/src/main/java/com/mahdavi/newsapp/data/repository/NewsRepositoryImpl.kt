package com.mahdavi.newsapp.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mahdavi.newsapp.data.dataSource.remote.RemoteDataSource
import com.mahdavi.newsapp.data.dataSource.remote.RemoteDataSourceImpl
import com.mahdavi.newsapp.data.model.remote.News
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import kotlinx.coroutines.Dispatchers
import com.mahdavi.newsapp.data.model.remote.MyError
import com.mahdavi.newsapp.utils.extensions.getApiError
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.io.IOException


class NewsRepositoryImpl(private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl.factory()) :
    NewsRepository {

    override suspend fun getNews(
        topic: String
    ): ResultWrapper<Exception, News?>? {
        return try {
            var result: ResultWrapper<Exception, News?>? = null
            remoteDataSource.getNews(topic)
                .flowOn(Dispatchers.IO)
                .onEach { response ->
                    result = ResultWrapper.build {
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            throw Exception(response.getApiError()?.message)
                        }
                    }
                }.collect()
            result
        } catch (e: Exception) {
            ResultWrapper.build {
                throw e
            }
        }
    }
}

