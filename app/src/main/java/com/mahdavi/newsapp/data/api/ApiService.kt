package com.mahdavi.newsapp.data.api

import com.mahdavi.newsapp.BuildConfig
import com.mahdavi.newsapp.data.model.remote.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    suspend fun getNews(
        @Query("q") topic: String,
        @Query("apiKey") key: String = BuildConfig.API_KEY
    ): Response<News>
}