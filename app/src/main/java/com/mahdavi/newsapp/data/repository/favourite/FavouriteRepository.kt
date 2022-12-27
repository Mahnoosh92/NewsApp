package com.mahdavi.newsapp.data.repository.favourite

import com.mahdavi.newsapp.data.dataSource.local.favourite.FavouriteLocalDataSource
import com.mahdavi.newsapp.data.dataSource.remote.source.SourceRemoteDataSource
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.news.HeadlineArticle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface FavouriteRepository {

    fun getLatestHeadlines(topic: String): Flow<ResultWrapper<Exception, List<HeadlineArticle>?>>
    fun updateFavouriteHeadlines(headline:HeadlineArticle): Flow<Unit>
    fun getFavouriteHeadlines(): Flow<ResultWrapper<Exception, List<HeadlineArticle>>>
}