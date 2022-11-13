package com.mahdavi.newsapp.data.repository

import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.MyError
import com.mahdavi.newsapp.data.model.remote.News

class FakeNewsRepositoryImpl:NewsRepository {
    override suspend fun getNews(topic: String): ResultWrapper<Exception, Pair<News?, MyError?>>? {
        return ResultWrapper.build {
            throw Exception()
        }
    }
}