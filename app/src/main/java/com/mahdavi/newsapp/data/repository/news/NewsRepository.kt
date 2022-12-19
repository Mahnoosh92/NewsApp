package com.mahdavi.newsapp.data.repository.news

import com.mahdavi.newsapp.data.model.local.HeadlineTitle
import com.mahdavi.newsapp.data.model.remote.news.HeadlineArticle
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.news.SearchedArticle
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getLatestHeadlines(topic: String): Flow<ResultWrapper<Exception, List<HeadlineArticle?>>?>
    fun searchNews(
        topic: String,
        from: String,
        countries: String = "US",
        page_size: Int
    ): Flow<ResultWrapper<Exception, List<SearchedArticle?>>?>

    fun getHeadlinesTitle(): Flow<List<HeadlineTitle>>
}