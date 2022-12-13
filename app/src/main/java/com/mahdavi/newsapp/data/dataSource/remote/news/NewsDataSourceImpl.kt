package com.mahdavi.newsapp.data.dataSource.remote.news

import com.mahdavi.newsapp.data.api.ApiService
import com.mahdavi.newsapp.data.dataSource.remote.news.NewsDataSource
import com.mahdavi.newsapp.data.model.remote.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class NewsDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    NewsDataSource {
    override fun getNews(topic: String): Flow<Response<News>> = flow {
        emit(apiService.getNews(topic))
    }
}