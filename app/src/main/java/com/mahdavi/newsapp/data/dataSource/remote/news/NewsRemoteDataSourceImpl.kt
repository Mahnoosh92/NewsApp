package com.mahdavi.newsapp.data.dataSource.remote.news

import com.mahdavi.newsapp.data.api.ApiService
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    NewsRemoteDataSource {
    override fun getLatestHeadlines(topic: String) =
        channelFlow { send(apiService.getLatestHeadlines(topic)) }
}