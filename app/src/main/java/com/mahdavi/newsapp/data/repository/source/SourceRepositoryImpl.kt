package com.mahdavi.newsapp.data.repository.source

import com.mahdavi.newsapp.data.dataSource.remote.source.SourceRemoteDataSource
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.SourceRemote
import com.mahdavi.newsapp.di.IoDispatcher
import com.mahdavi.newsapp.utils.extensions.getApiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SourceRepositoryImpl @Inject constructor(
    private val sourceRemoteDataSource: SourceRemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SourceRepository {
    override fun getSources(
        topic: String,
        lang: String,
        countries: String
    ): Flow<ResultWrapper<Exception, SourceRemote?>> = flow {
        sourceRemoteDataSource.getSources(topic = topic, lang = lang, countries = countries)
            .map { response ->
                if (response.isSuccessful) {
                    emit(ResultWrapper.build { response.body() })
                } else {
                    emit(ResultWrapper.build {
                        throw Exception(
                            response.getApiError()?.message ?: "Something went wrong!"
                        )
                    })
                }
            }
            .catch { error ->
                emit(ResultWrapper.build {
                    throw Exception(
                        error.message ?: "Something went wrong!"
                    )
                })
            }
            .flowOn(ioDispatcher)
            .collect()
    }

}