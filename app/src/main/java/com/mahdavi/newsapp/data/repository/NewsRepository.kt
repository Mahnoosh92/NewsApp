package com.mahdavi.newsapp.data.repository

import com.mahdavi.newsapp.data.model.local.Result
import com.mahdavi.newsapp.data.model.remote.Error
import com.mahdavi.newsapp.data.model.remote.News

interface NewsRepository {
    suspend fun getNews(
        topic: String
    ): Result<Exception, Pair<News?, Error?>>?
}