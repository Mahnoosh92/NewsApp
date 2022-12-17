package com.mahdavi.newsapp.data.dataSource.local.search

import com.mahdavi.newsapp.data.db.SearchDao
import com.mahdavi.newsapp.data.model.local.entity.SearchedNewsArticleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchLocalDataSourceImpl @Inject constructor(private val articleDao: SearchDao) :
    SearchLocalDataSource {
    override fun update(articles: List<SearchedNewsArticleEntity>): Flow<Unit> = flow {
        articleDao.getArticles()
        emit(Unit)
    }

    override fun getSearchedArticles(): Flow<List<SearchedNewsArticleEntity>> =
        articleDao.getArticles()

    override fun clear(): Flow<Unit> = flow {
        articleDao.clear()
        emit(Unit)
    }
}