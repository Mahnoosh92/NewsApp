package com.mahdavi.newsapp.data.repository

import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.MyError
import com.mahdavi.newsapp.data.model.remote.News

interface NewsRepository {
    suspend fun getNews(
        topic: String
    ): ResultWrapper<Exception, News?>?
}