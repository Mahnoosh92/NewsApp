package com.mahdavi.newsapp.data.dataSource.local.search


import com.mahdavi.newsapp.data.model.local.entity.SearchedNewsArticleEntity
import kotlinx.coroutines.flow.Flow

interface SearchLocalDataSource {
    fun update(articles: List<SearchedNewsArticleEntity>): Flow<Unit>

    fun getSearchedArticles(): Flow<List<SearchedNewsArticleEntity>>

    fun clear(): Flow<Unit>
}