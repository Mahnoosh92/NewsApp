package com.mahdavi.newsapp.data.repository.news.headline

import com.mahdavi.newsapp.data.model.HeadlineArticle
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface HeadlineRepository {
    suspend fun getLatestNews(topic: String): Flow<ResultWrapper<Exception, List<HeadlineArticle?>>?>
}