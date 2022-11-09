package com.mahdavi.newsapp.data.dataSource.remote

import com.mahdavi.newsapp.data.model.remote.News
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteDataSource {
    fun getNews(topic: String): Flow<Response<News>>
}