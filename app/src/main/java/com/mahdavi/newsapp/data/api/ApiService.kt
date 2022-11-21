package com.mahdavi.newsapp.data.api

import com.mahdavi.newsapp.BuildConfig
import com.mahdavi.newsapp.data.model.remote.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("latest_headlines")
    suspend fun getNews(
        @Query("topic") topic: String,
        @Query("countries") countries: String = "US"
    ): Response<News>
}


