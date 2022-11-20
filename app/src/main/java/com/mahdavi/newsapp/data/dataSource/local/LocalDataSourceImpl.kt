package com.mahdavi.newsapp.data.dataSource.local

import com.mahdavi.newsapp.data.db.ArticleDao
import com.mahdavi.newsapp.data.model.local.entity.Article
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val articleDao: ArticleDao) :
    LocalDataSource {
    override suspend fun insertAll(articles: List<Article>) = flow {
        articleDao.insertAll(articles)
        emit(Unit)
    }

    override fun getArticles() = articleDao.getArticles()
}