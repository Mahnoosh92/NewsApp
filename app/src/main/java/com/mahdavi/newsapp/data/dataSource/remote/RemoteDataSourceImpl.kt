package com.mahdavi.newsapp.data.dataSource.remote

import com.mahdavi.newsapp.data.api.ApiService
import com.mahdavi.newsapp.data.model.remote.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    RemoteDataSource {
    override fun getNews(topic: String): Flow<Response<News>> = flow {
        emit(apiService.getNews(topic))
    }
}