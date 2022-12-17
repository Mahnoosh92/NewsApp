package com.mahdavi.newsapp.data.dataSource.remote.news.headline

import com.mahdavi.newsapp.data.api.ApiService
import com.mahdavi.newsapp.data.model.remote.newsHeadline.NewsHeadLine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class HeadlineDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    HeadlineDataSource {
    override fun getLatestHeadlines(topic: String): Flow<Response<NewsHeadLine>> = flow {
        emit(apiService.getLatestHeadlines(topic))
    }
}