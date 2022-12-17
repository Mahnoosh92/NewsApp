package com.mahdavi.newsapp.data.dataSource.local.news

import com.mahdavi.newsapp.data.model.local.entity.NewsHeadlineArticleEntity
import kotlinx.coroutines.flow.Flow

interface NewsLocalDataSource {

    fun updateHeadlines(articles: List<NewsHeadlineArticleEntity>): Flow<Unit>

    fun getHeadlines(): Flow<List<NewsHeadlineArticleEntity>>

    fun clearHeadlines(): Flow<Unit>

    // TODO add search functions
}