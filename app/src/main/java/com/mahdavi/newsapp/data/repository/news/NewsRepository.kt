package com.mahdavi.newsapp.data.repository.news

import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.ArticleResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getNews(
        topic: String
    ): Flow<ResultWrapper<Exception, List<ArticleResponse?>>?>
}