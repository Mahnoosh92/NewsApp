package com.mahdavi.newsapp.data.repository.news

import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.data.dataSource.local.news.NewsLocalDataSource
import com.mahdavi.newsapp.data.dataSource.remote.news.NewsRemoteDataSource
import com.mahdavi.newsapp.data.model.local.HeadlineTitle
import com.mahdavi.newsapp.data.model.remote.news.HeadlineArticle
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.local.entity.HeadlineArticleEntity
import com.mahdavi.newsapp.data.model.local.entity.SearchedArticleEntity
import com.mahdavi.newsapp.data.model.remote.news.RemoteSearchedArticle
import com.mahdavi.newsapp.data.model.remote.news.SearchedArticle
import com.mahdavi.newsapp.di.IoDispatcher
import com.mahdavi.newsapp.utils.extensions.getApiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource,
    private val localDataSource: NewsLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : NewsRepository {

    override suspend fun getLatestHeadlines(topic: String): Flow<ResultWrapper<Exception, List<HeadlineArticle?>>?> =
        channelFlow {
            remoteDataSource.getLatestHeadlines(topic)
                .map { response ->
                    try {
                        if (!response.isSuccessful) {
                            throw Exception(response.getApiError()?.message)
                        }
                    } catch (e: Exception) {
                        send(ResultWrapper.build {
                            throw e
                        })
                    }
                    response.body()?.articles
                }
                .map { articleResponseList ->
                    clearHeadlines()
                        .onCompletion {
                            articleResponseList?.let {
                                updateHeadlines(it.filterNotNull()
                                    .map { articleResponse ->
                                        articleResponse.toHeadlineArticleEntity()
                                    })
                                    .collect()
                            }
                        }
                        .collect()
                }
                .map {
                    localDataSource.getHeadlines()
                        .catch { error ->
                            send(ResultWrapper.build {
                                throw error
                            })
                        }
                        .collect {
                            send(ResultWrapper.build {
                                it.map { article ->
                                    article.toHeadlineArticle()
                                }
                            })
                        }
                }
                .flowOn(ioDispatcher)
                .catch { error ->
                    send(ResultWrapper.build {
                        throw error
                    })
                }
                .collect()
        }

    override fun searchNews(
        topic: String,
        from: String,
        countries: String,
        page_size: Int
    ): Flow<ResultWrapper<Exception, List<SearchedArticle?>>?> = channelFlow {
        remoteDataSource.searchNews(
            topic = topic,
            from = from,
            countries = countries,
            page_size = page_size
        )
            .map { response ->
                if (!response.isSuccessful) {
                    send(ResultWrapper.build {
                        throw Exception(response.getApiError()?.message)
                    })
                }
                response.body()?.articles
            }
            .map { listarticles ->
                listarticles?.let { list ->
                    clearSearchedArticles()
                        .onCompletion {
                            updateSearchedArticles(
                                list.filterNotNull()
                                    .map(RemoteSearchedArticle::toSearchedArticleEntity)
                            )
                                .catch { error -> Timber.e(error) }
                                .collect()
                        }
                        .catch { error -> Timber.e(error) }
                        .collect()
                }
            }
            .map {
                localDataSource.getSearchedArticles()
                    .catch { error ->
                        send(ResultWrapper.build {
                            throw error
                        })
                    }
                    .collect { list ->
                        send(ResultWrapper.build {
                            list.map {
                                it.toSearchedArticle()
                            }
                        })
                    }
            }
            .catch { error ->
                send(ResultWrapper.build {
                    throw error
                })
            }
            .collect()

    }

    override fun getHeadlinesTitle() = localDataSource.getHeadlinesTitle()


    private fun clearHeadlines() = localDataSource.clearHeadlines()
    private fun updateHeadlines(list: List<HeadlineArticleEntity>) =
        localDataSource.updateHeadlines(list)

    private fun clearSearchedArticles() = localDataSource.clearSearchedArticles()
    private fun updateSearchedArticles(list: List<SearchedArticleEntity>) =
        localDataSource.updateSearchedArticles(list)
}

