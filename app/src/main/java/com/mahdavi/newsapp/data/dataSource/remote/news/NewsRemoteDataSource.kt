package com.mahdavi.newsapp.data.dataSource.remote.news

import com.mahdavi.newsapp.data.model.remote.news.HeadLineNews
import com.mahdavi.newsapp.data.model.remote.news.SearchedNews
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface NewsRemoteDataSource {
    fun getLatestHeadlines(topic: String): Flow<Response<HeadLineNews>>
    fun searchNews(topic: String, from: String, countries: String = "US", page_size: Int): Flow<Response<SearchedNews>>
}