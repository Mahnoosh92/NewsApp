package com.mahdavi.newsapp.data.api

import com.mahdavi.newsapp.data.model.remote.SourceRemote
import com.mahdavi.newsapp.data.model.remote.news.HeadLineNews
import com.mahdavi.newsapp.data.model.remote.news.SearchedNews
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("latest_headlines")
    suspend fun getLatestHeadlines(
        @Query("topic") topic: String,
        @Query("countries") countries: String = "US"
    ): Response<HeadLineNews>

    @GET("search")
    suspend fun searchNews(
        @Query("q") topic: String,
        @Query("from") from: String,
        @Query("countries") countries: String = "US",
        @Query("page_size") page_size: Int
    ): Response<SearchedNews>

    @GET("sources")
    suspend fun getSources(
        @Query("topic") topic: String,
        @Query("lang") lang: String,
        @Query("countries") countries: String,
    ):Response<SourceRemote>
}


