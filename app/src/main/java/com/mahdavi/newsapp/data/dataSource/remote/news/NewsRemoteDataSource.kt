package com.mahdavi.newsapp.data.dataSource.remote.news

import com.mahdavi.newsapp.data.model.remote.newsHeadline.NewsHeadLine
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface NewsRemoteDataSource {
    fun getLatestHeadlines(topic: String): Flow<Response<NewsHeadLine>>
}