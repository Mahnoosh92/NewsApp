package com.mahdavi.newsapp.data.repository

import com.mahdavi.newsapp.data.api.Api
import com.mahdavi.newsapp.data.api.RetrofitClient
import com.mahdavi.newsapp.data.model.remote.News
import com.mahdavi.newsapp.data.model.local.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.mahdavi.newsapp.data.model.remote.Error


class NewsRepository(private val api: Api = RetrofitClient.getInstance()) {

    suspend fun getNews(
        topic: String
    ): Result<Exception, Pair<News?, Error?>> = withContext(Dispatchers.IO) {
        try {
            val request = api.getNews(topic)
            if (request.isSuccessful) {
                Result.build {
                    request.body() to null
                }
            } else {
                Result.build {
                    null to null
                }
            }
        } catch (exception: Exception) {
            Result.build {
                throw exception
            }
        }
    }
}