package com.mahdavi.newsapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.news.SearchedArticle
import com.mahdavi.newsapp.data.repository.news.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor( private val newsRepository: NewsRepository):ViewModel() {

    private val _articles = MutableStateFlow(SearchUiState())
    val articles = _articles.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _articles.update {
            it.copy(error = exception.message.toString())
        }
    }

    fun getQuery(input: StateFlow<String>) {
        viewModelScope.launch(exceptionHandler) {
            input
                .debounce(1000L)
                .distinctUntilChanged()
                .filter {
                    it.isNotEmpty()
                }
                .flatMapLatest { topic ->
                    newsRepository.searchNews(topic=topic, from = "2022/12/15", countries = "US", page_size = 1)
                }
                .catch { error ->
                    Timber.e(error)
                    _articles.update { homeUiState ->
                        homeUiState.copy(
                            error = error.message, isLoading = false
                        )
                    }
                }.collect { result ->
                    when (result) {
                        is ResultWrapper.Value -> {
                            _articles.update { searchUiState ->
                                searchUiState.copy(
                                    data = result.value, isLoading = false
                                )
                            }
                        }
                        is ResultWrapper.Error -> {
                            _articles.update { searchUiState ->
                                searchUiState.copy(
                                    error = result.error.message, isLoading = false
                                )
                            }
                        }
                        else -> {
                            throw  IllegalArgumentException("wrong args")
                        }
                    }
                }
        }
    }
}


data class SearchUiState(
    val data: List<SearchedArticle?>? = null,
    val error: String? = null,
    val isLoading: Boolean? = null
)