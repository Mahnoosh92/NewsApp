package com.mahdavi.newsapp.data.dataSource.local

import com.mahdavi.newsapp.data.db.ArticleDao
import com.mahdavi.newsapp.data.model.local.entity.Article
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val articleDao: ArticleDao) :
    LocalDataSource {

    override fun update(articles: List<Article>) = flow {
        articleDao.insertAll(articles)
        emit(Unit)
    }

    override fun getArticles() = articleDao.getArticles()

    override fun clear() = flow {
        articleDao.clear()
        emit(Unit)
    }

}