package com.mahdavi.newsapp.ui.home

import androidx.lifecycle.*
import com.mahdavi.newsapp.data.model.local.NetworkResult
import com.mahdavi.newsapp.data.model.remote.ArticleResponse
import com.mahdavi.newsapp.data.model.local.ResultWrapper.Error
import com.mahdavi.newsapp.data.model.local.ResultWrapper.Value
import com.mahdavi.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private var _articles =
        MutableStateFlow<NetworkResult<List<ArticleResponse?>>>(NetworkResult.Loading())
    val articles: StateFlow<NetworkResult<List<ArticleResponse?>>>
        get() = _articles.asStateFlow()


    init {
        getNews()
    }


    private fun getNews(topic: String = "bitcoin") {
        viewModelScope.launch {
            newsRepository.getArticles(topic).onEach { result ->
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
            }.collect()
        }
    }
}

class HomeViewModelFactory(private val newsRepository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return HomeViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}