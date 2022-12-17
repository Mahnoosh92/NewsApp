package com.mahdavi.newsapp.data.dataSource.remote.news.search

import com.mahdavi.newsapp.data.api.ApiService
import com.mahdavi.newsapp.data.model.remote.searchedNews.SearchedNews
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class SearchedArticleDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    SearchedArticleDataSource {
    override fun searchNews(
        topic: String,
        from: String,
        countries: String,
        page_size: Int
    ): Flow<Response<SearchedNews>> = flow {
        emit(apiService.searchNews(topic, from, countries, page_size))
    }
}