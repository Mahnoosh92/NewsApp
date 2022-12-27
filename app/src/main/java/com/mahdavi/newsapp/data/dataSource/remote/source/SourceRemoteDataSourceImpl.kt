package com.mahdavi.newsapp.data.dataSource.remote.source

import com.mahdavi.newsapp.data.api.ApiService
import com.mahdavi.newsapp.data.model.remote.SourceRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class SourceRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService):SourceRemoteDataSource{
    override fun getSources(
        topic: String,
        lang: String,
        countries: String
    ): Flow<Response<SourceRemote>> = flow {
        emit(apiService.getSources(topic = topic, lang = lang, countries = countries))
    }
}