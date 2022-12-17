package com.mahdavi.newsapp.data.repository.news.search

import com.mahdavi.newsapp.data.model.HeadlineArticle
import com.mahdavi.newsapp.data.model.SearchedArticle
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchNews(
        topic: String,
        from: String,
        countries: String,
        page_size: Int
    ): Flow<ResultWrapper<Exception, List<SearchedArticle?>>?>
}