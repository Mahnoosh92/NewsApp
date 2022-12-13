package com.mahdavi.newsapp.data.repository.news

import com.mahdavi.newsapp.data.dataSource.local.LocalDataSource
import com.mahdavi.newsapp.data.dataSource.remote.news.NewsDataSource
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.local.entity.Article
import com.mahdavi.newsapp.data.model.remote.ArticleResponse
import com.mahdavi.newsapp.di.IoDispatcher
import com.mahdavi.newsapp.utils.extensions.getApiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsDataSource,
    private val localDataSource: LocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NewsRepository {

    override suspend fun getNews(topic: String): Flow<ResultWrapper<Exception, List<ArticleResponse?>>?> =
        flow {
            remoteDataSource.getNews(topic)
                .flowOn(ioDispatcher)
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
                        .flowOn(ioDispatcher)
                        .onCompletion {
                            articleResponseList?.let {
                                updateDataBase(it.filterNotNull()
                                    .map { articleResponse ->
                                        articleResponse.toArticle()
                                    })
                                    .flowOn(ioDispatcher)
                                    .catch { error -> Timber.e(error) }
                                    .collect()
                            }
                        }
                        .catch { error -> Timber.e(error) }
                        .collect()
                }
                .map {
                    localDataSource.getArticles()
                        .flowOn(ioDispatcher)
                        .onEach {
                            emit(ResultWrapper.build {
                                it.map { article ->
                                    article.toArticleResponse()
                                }
                            })
                        }
                        .catch { error ->
                            emit(ResultWrapper.build {
                                throw error
                            })
                        }.collect()
                }
                .catch { error ->
                    emit(ResultWrapper.build {
                        throw error
                    })
                }
                .collect()
        }

    private fun clearDataBase() = localDataSource.clear()
    private fun updateDataBase(list: List<Article>) = localDataSource.update(list)
}

