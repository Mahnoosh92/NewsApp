package com.mahdavi.newsapp.data.repository.favourite

import com.mahdavi.newsapp.data.dataSource.local.favourite.FavouriteLocalDataSource
import com.mahdavi.newsapp.data.dataSource.remote.news.NewsRemoteDataSource
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.local.entity.FavouriteHeadLineArticleEntity
import com.mahdavi.newsapp.data.model.remote.news.HeadlineArticle
import com.mahdavi.newsapp.data.model.remote.news.RemoteHeadlineArticle
import com.mahdavi.newsapp.di.IoDispatcher
import com.mahdavi.newsapp.utils.extensions.getApiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val favouriteLocalDataSource: FavouriteLocalDataSource,
    private val newsRemoteDataSource: NewsRemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FavouriteRepository {
    override fun getLatestHeadlines(topic: String): Flow<ResultWrapper<Exception, List<HeadlineArticle?>?>> {
        return newsRemoteDataSource.getLatestHeadlines(topic = topic)
            .map { response ->
                if (response.isSuccessful) {
                    ResultWrapper.build {
                        response.body()?.articles?.map {
                            it?.toHeadlineArticle()
                        }
                    }
                } else {
                    ResultWrapper.build {
                        throw Exception(response.getApiError()?.message)
                    }
                }

            }
            .catch {
                ResultWrapper.build {
                    throw Exception(it.message)
                }
            }
            .flowOn(ioDispatcher)
    }

//    override fun getLatestHeadlines(topic: String): Flow<ResultWrapper<Exception, List<HeadlineArticle>?>> =
//        flow {
//            newsRemoteDataSource.getLatestHeadlines(topic)
//                .map { response ->
//                    if (response.isSuccessful) {
//                        emit(ResultWrapper.build {
//                            response.body()?.articles?.filterNotNull()
//                                ?.map(RemoteHeadlineArticle::toHeadlineArticle)
//                        })
//                    } else {
//                        ResultWrapper.build { throw Exception(response.getApiError()?.message) }
//                    }
//                }
//                .catch { error -> ResultWrapper.build { throw Exception(error.message) } }
//                .collect()
//        }

    override fun updateFavouriteHeadlines(headline: HeadlineArticle) =
        favouriteLocalDataSource.updateFavouriteHeadlines(headline.toFavouriteHeadlineArticleEntity())

    override fun getFavouriteHeadlines(): Flow<ResultWrapper<Exception, List<HeadlineArticle>>> {
        return favouriteLocalDataSource.getFavouriteHeadlines()
            .map {
                ResultWrapper.build {
                    it.map { entity ->
                        entity.toHeadlineArticle()
                    }
                }
            }
            .catch {
                ResultWrapper.build {
                    throw Exception(it.message)
                }
            }
    }

//    override fun getFavouriteHeadlines(): Flow<ResultWrapper<Exception, List<HeadlineArticle>>> =
//        flow {
//            favouriteLocalDataSource.getFavouriteHeadlines()
//                .map { list ->
//                    emit(ResultWrapper.build {
//                        list.filterNotNull().map(FavouriteHeadLineArticleEntity::toHeadlineArticle)
//                    })
//                }
//                .catch { error -> emit(ResultWrapper.build { throw error }) }
//                .collect()
//        }
}