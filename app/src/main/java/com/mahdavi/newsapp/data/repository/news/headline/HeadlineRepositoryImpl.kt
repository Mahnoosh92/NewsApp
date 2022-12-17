package com.mahdavi.newsapp.data.repository.news.headline

import com.mahdavi.newsapp.data.dataSource.local.headline.HeadlineLocalDataSource
import com.mahdavi.newsapp.data.dataSource.remote.news.headline.HeadlineDataSource
import com.mahdavi.newsapp.data.model.HeadlineArticle
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.local.entity.NewsHeadlineArticleEntity
import com.mahdavi.newsapp.data.model.remote.newsHeadline.NewsHeadlineArticle
import com.mahdavi.newsapp.di.IoDispatcher
import com.mahdavi.newsapp.utils.extensions.getApiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class HeadlineRepositoryImpl @Inject constructor(
    private val remoteDataSource: HeadlineDataSource,
    private val localDataSource: HeadlineLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : HeadlineRepository {
    override suspend fun getLatestNews(topic: String): Flow<ResultWrapper<Exception, List<HeadlineArticle?>>?> =
        flow {
            remoteDataSource.getLatestHeadlines(topic)
                .map { response ->
                    try {
                        if (!response.isSuccessful) {
                            throw Exception(response.getApiError()?.message)
                        }
                    } catch (e: Exception) {
                        emit(ResultWrapper.build {
                            throw e
                        })
                    }
                    response.body()?.articles
                }
                .map { articleResponseList ->
                    clearDataBase()
                        .onCompletion {
                            articleResponseList?.let {
                                updateDataBase(
                                    it.filterNotNull()
                                        .map(NewsHeadlineArticle::toNewsHeadlineArticleEntity)
                                )
                                    .flowOn(ioDispatcher)
                                    .catch { error -> Timber.e(error) }
                                    .collect()
                            }
                        }
                        .flowOn(ioDispatcher)
                        .catch { error -> Timber.e(error) }
                        .collect()
                }
                .map {
                    localDataSource.getHeadlineArticles()
                        .flowOn(ioDispatcher)
                        .catch { error ->
                            Timber.e(error)
                        }
                        .collect {
                            emit(ResultWrapper.build {
                                it.map { article ->
                                    article.toHeadlineArticle()
                                }
                            })
                        }
                }
                .flowOn(ioDispatcher)
                .catch { error ->
                    emit(ResultWrapper.build {
                        throw error
                    })
                }
                .collect()
        }

    private fun clearDataBase() = localDataSource.clear()
    private fun updateDataBase(list: List<NewsHeadlineArticleEntity>) = localDataSource.update(list)
}

