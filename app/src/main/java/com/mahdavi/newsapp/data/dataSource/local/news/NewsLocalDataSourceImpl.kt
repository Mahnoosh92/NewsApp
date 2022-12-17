package com.mahdavi.newsapp.data.dataSource.local.news

import com.mahdavi.newsapp.data.db.HeadlineDao
import com.mahdavi.newsapp.data.model.local.entity.NewsHeadlineArticleEntity
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsLocalDataSourceImpl @Inject constructor(private val articleDao: HeadlineDao) :
    NewsLocalDataSource {
    override fun updateHeadlines(articles: List<NewsHeadlineArticleEntity>) = flow {
        articleDao.insertAll(articles)
        emit(Unit)
    }

    override fun getHeadlines() = articleDao.getArticles()

    override fun clearHeadlines() = flow {
        articleDao.clear()
        emit(Unit)
    }

    // TODO add search functions
}