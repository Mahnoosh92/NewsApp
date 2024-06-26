package com.mahdavi.newsapp.data.api

import com.mahdavi.newsapp.data.model.remote.newsHeadline.NewsHeadLine
import com.mahdavi.newsapp.data.model.remote.searchedNews.SearchedNews
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("latest_headlines")
    suspend fun getLatestHeadlines(
        @Query("topic") topic: String,
        @Query("countries") countries: String = "US"
    ): Response<NewsHeadLine>

    @GET("search")
    suspend fun searchNews(
        @Query("q") topic: String,
        @Query("from") from: String,
        @Query("countries") countries: String = "US",
        @Query("page_size") page_size: Int
    ): Response<SearchedNews>
}


