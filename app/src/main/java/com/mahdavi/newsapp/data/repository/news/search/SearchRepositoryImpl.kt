package com.mahdavi.newsapp.data.repository.news.search

import com.mahdavi.newsapp.data.dataSource.local.search.SearchLocalDataSource
import com.mahdavi.newsapp.data.dataSource.remote.news.search.SearchedArticleDataSource
import com.mahdavi.newsapp.data.model.SearchedArticle
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.local.entity.NewsHeadlineArticleEntity
import com.mahdavi.newsapp.data.model.local.entity.SearchedNewsArticleEntity
import com.mahdavi.newsapp.data.model.remote.searchedNews.SearchedNewsArticle
import com.mahdavi.newsapp.di.IoDispatcher
import com.mahdavi.newsapp.utils.extensions.getApiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: SearchedArticleDataSource,
    private val localDataSource: SearchLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SearchRepository {
    override suspend fun searchNews(
        topic: String,
        from: String,
        countries: String,
        page_size: Int
    ): Flow<ResultWrapper<Exception, List<SearchedArticle?>>?> = flow {
        remoteDataSource.searchNews(topic, from, countries, page_size)
            .map { response ->
                if (!response.isSuccessful) {
                    emit(ResultWrapper.build {
                        throw Exception(response.getApiError()?.message ?: "Something went wrong!")
                    })
                }
                response.body()?.articles
            }
            .map { listSearchNewsArticle ->
                clearDataBase()
                    .onCompletion {
                        val list = listSearchNewsArticle?.filterNotNull()
                        list?.let {
                            updateDataBase(
                                it.map(SearchedNewsArticle::toSearchedNewsArticleEntity)
                            )
                        }
                    }
                    .flowOn(ioDispatcher)
                    .catch { error -> Timber.e(error.message) }
                    .collect()
            }
            .map {
                localDataSource.getSearchedArticles()
                    .map { list ->
                        emit(ResultWrapper.build {
                            list.map(SearchedNewsArticleEntity::toSearchedArticle)
                        })
                    }
            }
            .flowOn(ioDispatcher)
            .catch { error -> Timber.e(error.message) }
            .collect()
    }

    private fun clearDataBase() = localDataSource.clear()
    private fun updateDataBase(list: List<SearchedNewsArticleEntity>) = localDataSource.update(list)
}