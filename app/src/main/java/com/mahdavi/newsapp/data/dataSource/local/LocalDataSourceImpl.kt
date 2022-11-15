package com.mahdavi.newsapp.data.dataSource.local

import com.mahdavi.newsapp.NewsApplication
import com.mahdavi.newsapp.data.db.ArticleDao
import com.mahdavi.newsapp.data.model.local.entity.Article
import kotlinx.coroutines.flow.flow

class LocalDataSourceImpl(private val articleDao: ArticleDao = NewsApplication.application().database.articleDao()) :
    LocalDataSource {
    override suspend fun insertAll(articles: List<Article>) = flow {
        articleDao.insertAll(articles)
        emit(Unit)
    }

    override fun getArticles() = articleDao.getArticles()
}