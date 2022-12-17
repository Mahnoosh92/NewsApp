package com.mahdavi.newsapp.data.dataSource.remote.news.search

import com.mahdavi.newsapp.data.model.remote.newsHeadline.NewsHeadLine
import com.mahdavi.newsapp.data.model.remote.searchedNews.SearchedNews
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface SearchedArticleDataSource {
    fun searchNews(
        topic: String,
        from: String,
        countries: String,
        page_size: Int
    ): Flow<Response<SearchedNews>>
}