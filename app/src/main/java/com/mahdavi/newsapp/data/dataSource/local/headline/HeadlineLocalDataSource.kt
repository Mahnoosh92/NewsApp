package com.mahdavi.newsapp.data.dataSource.local.headline

import com.mahdavi.newsapp.data.model.local.entity.NewsHeadlineArticleEntity
import kotlinx.coroutines.flow.Flow

interface HeadlineLocalDataSource {

    fun update(articles: List<NewsHeadlineArticleEntity>): Flow<Unit>

    fun getHeadlineArticles(): Flow<List<NewsHeadlineArticleEntity>>

    fun clear(): Flow<Unit>
}