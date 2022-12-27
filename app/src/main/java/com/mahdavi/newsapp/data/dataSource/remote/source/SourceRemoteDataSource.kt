package com.mahdavi.newsapp.data.dataSource.remote.source

import com.mahdavi.newsapp.data.model.remote.SourceRemote
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Query

interface SourceRemoteDataSource {

    fun getSources(topic: String, lang: String, countries: String): Flow<Response<SourceRemote>>
}