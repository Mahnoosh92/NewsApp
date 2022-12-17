package com.mahdavi.newsapp.data.dataSource.local.headline

import com.mahdavi.newsapp.data.db.HeadlineDao
import com.mahdavi.newsapp.data.model.local.entity.NewsHeadlineArticleEntity
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HeadlineLocalDataSourceImpl @Inject constructor(private val articleDao: HeadlineDao) :
    HeadlineLocalDataSource {

    override fun update(articles: List<NewsHeadlineArticleEntity>) = flow {
        articleDao.insertAll(articles)
        emit(Unit)
    }

    override fun getHeadlineArticles() = articleDao.getArticles()

    override fun clear() = flow {
        articleDao.clear()
        emit(Unit)
    }

}