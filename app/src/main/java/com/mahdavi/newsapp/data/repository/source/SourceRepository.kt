package com.mahdavi.newsapp.data.repository.source

import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.SourceRemote
import kotlinx.coroutines.flow.Flow

interface SourceRepository {
    fun getSources(topic: String, lang: String, countries: String): Flow<ResultWrapper<Exception,SourceRemote?>>
}