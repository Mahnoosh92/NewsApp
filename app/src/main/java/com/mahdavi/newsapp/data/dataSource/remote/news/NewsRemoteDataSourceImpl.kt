package com.mahdavi.newsapp.data.dataSource.remote.news

import com.mahdavi.newsapp.data.api.ApiService
import com.mahdavi.newsapp.data.model.remote.news.SearchedNews
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class NewsRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    NewsRemoteDataSource {
    override fun getLatestHeadlines(topic: String) =
        channelFlow { send(apiService.getLatestHeadlines(topic)) }

    override fun searchNews(
        topic: String,
        from: String,
        countries: String,
        page_size: Int
    ) = channelFlow {
        send(
            apiService.searchNews(
                topic = topic,
                from = from,
                countries = countries,
                page_size = page_size
            )
        )
    }
}