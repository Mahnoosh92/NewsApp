package com.mahdavi.newsapp.data.dataSource.local.news

import com.mahdavi.newsapp.data.db.HeadlineDao
import com.mahdavi.newsapp.data.model.local.entity.NewsHeadlineArticleEntity
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsLocalDataSourceImpl @Inject constructor(private val articleDao: HeadlineDao) :
    NewsLocalDataSource {
    override fun updateHeadlines(articles: List<NewsHeadlineArticleEntity>) = channelFlow {
        articleDao.insertAll(articles)
        send(Unit)
    }

    override fun getHeadlines() = articleDao.getArticles()

    override fun clearHeadlines() = channelFlow {
        articleDao.clear()
        send(Unit)
    }

    // TODO add search functions
}