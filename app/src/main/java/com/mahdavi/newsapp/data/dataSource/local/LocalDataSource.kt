package com.mahdavi.newsapp.data.dataSource.local

import com.mahdavi.newsapp.data.model.local.entity.Article
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun insertAll(articles: List<Article>): Flow<Unit>

    fun getArticles(): Flow<List<Article>>
}