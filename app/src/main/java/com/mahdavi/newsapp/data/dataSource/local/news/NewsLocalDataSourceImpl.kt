package com.mahdavi.newsapp.data.dataSource.local.news

import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.data.db.HeadlineDao
import com.mahdavi.newsapp.data.db.SearchDao
import com.mahdavi.newsapp.data.model.local.HeadlineTitle
import com.mahdavi.newsapp.data.model.local.entity.HeadlineArticleEntity
import com.mahdavi.newsapp.data.model.local.entity.SearchedArticleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class NewsLocalDataSourceImpl @Inject constructor(
    private val headlineDao: HeadlineDao, private val searchDao: SearchDao
) : NewsLocalDataSource {
    override fun updateHeadlines(articles: List<HeadlineArticleEntity>) = channelFlow {
        headlineDao.insertAll(articles)
        send(Unit)
    }

    override fun getHeadlines() = headlineDao.getArticles()

    override fun clearHeadlines() = channelFlow {
        headlineDao.clear()
        send(Unit)
    }

    override fun updateSearchedArticles(articles: List<SearchedArticleEntity>) = channelFlow {
        searchDao.insertAll(articles)
        send(Unit)
    }

    override fun getSearchedArticles() = searchDao.getArticles()

    override fun clearSearchedArticles(): Flow<Unit> = channelFlow {
        searchDao.clear()
        send(Unit)
    }

    override fun getHeadlinesTitle(): Flow<List<HeadlineTitle>> = channelFlow {
        send(
            listOf(
                HeadlineTitle("news", true),
                HeadlineTitle("sport", isSelected = false),
                HeadlineTitle("tech", isSelected = false),
                HeadlineTitle("world", isSelected = false),
                HeadlineTitle("finance", isSelected = false),

            )
        )
    }
}