package com.mahdavi.newsapp.data.repository.news

import com.mahdavi.newsapp.data.dataSource.local.news.NewsLocalDataSource
import com.mahdavi.newsapp.data.dataSource.remote.news.NewsRemoteDataSource
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

class NewsRepositoryImpl @Inject constructor(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NewsRepository {
    override suspend fun getLatestHeadlines(topic: String): Flow<ResultWrapper<Exception, List<HeadlineArticle?>>?> =
        flow {
            newsRemoteDataSource.getLatestHeadlines(topic)
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
                    clearLocalHeadlines()
                        .onCompletion {
                            articleResponseList?.let {
                                updateLocalHeadlines(it.filterNotNull()
                                    .map { articleResponse ->
                                        articleResponse.toNewsHeadlineArticleEntity()
                                    })
                                    .flowOn(ioDispatcher)
                                    .catch { error ->
                                        Timber.e(error)
                                    }
                                    .collect()
                            }
                        }
                        .flowOn(ioDispatcher)
                        .catch { error ->
                            Timber.e(error)
                        }
                        .collect()
                }
                .map {
                    getLocalHeadLines()
                        .onEach {
                            emit(ResultWrapper.build {
                                it.map { article ->
                                    article.toHeadlineArticle()
                                }
                            })
                        }
                        .flowOn(ioDispatcher)
                        .catch { error ->
                            Timber.e(error)
                        }
                        .collect()
                }
                .flowOn(ioDispatcher)
                .catch { error ->
                    emit(ResultWrapper.build {
                        throw error
                    })
                }
                .collect()
        }

    private fun clearLocalHeadlines() = newsLocalDataSource.clearHeadlines()
    private fun updateLocalHeadlines(list: List<NewsHeadlineArticleEntity>) =
        newsLocalDataSource.updateHeadlines(list)
    private fun getLocalHeadLines() = newsLocalDataSource.getHeadlines()
}