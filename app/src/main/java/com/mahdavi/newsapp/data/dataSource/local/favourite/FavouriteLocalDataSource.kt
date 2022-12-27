package com.mahdavi.newsapp.data.dataSource.local.favourite

import com.mahdavi.newsapp.data.model.local.entity.FavouriteHeadLineArticleEntity

import kotlinx.coroutines.flow.Flow

interface FavouriteLocalDataSource {
    fun updateFavouriteHeadlines(headline: FavouriteHeadLineArticleEntity): Flow<Unit>

    fun getFavouriteHeadlines(): Flow<List<FavouriteHeadLineArticleEntity>>

    fun clearFavouriteHeadlines(): Flow<Unit>
}