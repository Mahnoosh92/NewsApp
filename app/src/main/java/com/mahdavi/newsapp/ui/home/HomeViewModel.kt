package com.mahdavi.newsapp.ui.home

import androidx.lifecycle.*
import com.mahdavi.newsapp.data.model.local.NetworkResult
import com.mahdavi.newsapp.data.model.remote.ArticleResponse
import com.mahdavi.newsapp.data.model.local.ResultWrapper.Error
import com.mahdavi.newsapp.data.model.local.ResultWrapper.Value
import com.mahdavi.newsapp.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {

    private var _articles =
        MutableStateFlow<NetworkResult<List<ArticleResponse?>>>(NetworkResult.Loading())
    val articles: StateFlow<NetworkResult<List<ArticleResponse?>>>
        get() = _articles.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _articles.value = NetworkResult.Error(message = exception.message)
    }

    init {
        getNews()
    }

    private fun getNews(topic: String = "business") {
        viewModelScope.launch(exceptionHandler) {
            newsRepository.getArticles(topic)
                .onEach { result ->
                    when (result) {
                        is Value -> {
                            _articles.value = NetworkResult.Success(result.value)
                        }
                        is Error -> {
                            _articles.value = NetworkResult.Error(message = result.error.message)
                        }
                        else -> {
                            throw  IllegalArgumentException("wrong args")
                        }
                    }
                }.catch { error -> Timber.e(error)
                    _articles.value = NetworkResult.Error(message = error.message)}
                .collect()
        }
    }
}

