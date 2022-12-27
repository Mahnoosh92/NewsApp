package com.mahdavi.newsapp.data.dataSource.local.favourite

import com.mahdavi.newsapp.data.db.FavouriteHeadlineDao
import com.mahdavi.newsapp.data.model.local.entity.FavouriteHeadLineArticleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FavouriteLocalDataSourceImpl @Inject constructor(private val favouriteHeadlineDao: FavouriteHeadlineDao) :
    FavouriteLocalDataSource {
    override fun updateFavouriteHeadlines(headline: FavouriteHeadLineArticleEntity): Flow<Unit> =
        flow {
            favouriteHeadlineDao.insert(headline)
            emit(Unit)
        }

    override fun getFavouriteHeadlines(): Flow<List<FavouriteHeadLineArticleEntity>> =
        favouriteHeadlineDao.getFavouriteHeadlines()

    override fun clearFavouriteHeadlines() = flow {
        favouriteHeadlineDao.clear()
        emit(Unit)
    }
}