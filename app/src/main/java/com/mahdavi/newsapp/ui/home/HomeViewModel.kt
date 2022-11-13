package com.mahdavi.newsapp.ui.home

import androidx.lifecycle.*
import com.mahdavi.newsapp.data.model.local.NetworkResult
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.Article
import com.mahdavi.newsapp.data.model.local.ResultWrapper.Error
import com.mahdavi.newsapp.data.model.local.ResultWrapper.Value
import com.mahdavi.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HomeViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private var _articles = MutableStateFlow<NetworkResult<List<Article?>>>(NetworkResult.Loading())
    private val articles = _articles.asStateFlow()


    init {
        getNews()
    }

    fun getNews(topic: String = "bitcoin") {
        viewModelScope.launch {
            when (val result = newsRepository.getNews(topic)) {
                is Value -> {
                    result.value?.articles?.let {
                        _articles.value = NetworkResult.Success(it)
                    } ?: run { _articles.value = NetworkResult.Success(emptyList()) }
                }
                is Error -> {
                    _articles.value = NetworkResult.Error(message = result.error.message)
                }
                else -> {
                    throw  IllegalArgumentException("wrong args")
                }
            }
        }
    }
}

class HomeViewModelFactory(private val newsRepository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModelFactory::class.java)) {
            @Suppress("UNCHECKED_CAST") return HomeViewModelFactory(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}