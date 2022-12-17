package com.mahdavi.newsapp.data.repository.news

import com.mahdavi.newsapp.data.model.HeadlineArticle
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getLatestHeadlines(topic: String): Flow<ResultWrapper<Exception, List<HeadlineArticle?>>?>
}