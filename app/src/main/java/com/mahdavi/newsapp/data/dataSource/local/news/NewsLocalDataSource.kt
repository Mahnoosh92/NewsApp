package com.mahdavi.newsapp.data.dataSource.local.news

import com.mahdavi.newsapp.data.model.local.HeadlineTitle
import com.mahdavi.newsapp.data.model.local.entity.HeadlineArticleEntity
import com.mahdavi.newsapp.data.model.local.entity.SearchedArticleEntity
import kotlinx.coroutines.flow.Flow

interface NewsLocalDataSource {

    fun updateHeadlines(articles: List<HeadlineArticleEntity>): Flow<Unit>

    fun getHeadlines(): Flow<List<HeadlineArticleEntity>>

    fun clearHeadlines(): Flow<Unit>

    fun updateSearchedArticles(articles: List<SearchedArticleEntity>): Flow<Unit>

    fun getSearchedArticles(): Flow<List<SearchedArticleEntity>>

    fun clearSearchedArticles(): Flow<Unit>

    fun getHeadlinesTitle(): Flow<List<HeadlineTitle>>

}