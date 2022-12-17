package com.mahdavi.newsapp.ui.headlines

import android.os.Parcelable
import androidx.lifecycle.*
import com.mahdavi.newsapp.data.model.HeadlineArticle
import com.mahdavi.newsapp.data.model.local.ResultWrapper.Error
import com.mahdavi.newsapp.data.model.local.ResultWrapper.Value
import com.mahdavi.newsapp.data.repository.news.NewsRepository
import com.mahdavi.newsapp.data.repository.news.headline.HeadlineRepository
import com.mahdavi.newsapp.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HeadlineViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _articles = MutableStateFlow(HomeUiState())
    val articles = _articles.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _articles.update {
            it.copy(error = exception.message.toString())
        }
    }

    fun getQuery(input: StateFlow<String>) {
        viewModelScope.launch(exceptionHandler) {
            input
                .debounce(2000L)
                .distinctUntilChanged()
                .filter {
                    it.isNotEmpty()
                }
                .flatMapLatest { topic ->
                    newsRepository.getLatestHeadlines(topic)
                }
                .catch { error ->
                    Timber.e(error)
                    _articles.update { homeUiState ->
                        homeUiState.copy(
                            error = error.message, loading = false
                        )
                    }
                }.collect { result ->
                    when (result) {
                        is Value -> {
                            _articles.update { homeUiState ->
                                homeUiState.copy(
                                    data = result.value.filterNotNull().map {
                                        ItemArticleUiState(it) {
                                            //TODO:R&D
                                        }
                                    }, loading = false
                                )
                            }
                        }
                        is Error -> {
                            _articles.update { homeUiState ->
                                homeUiState.copy(
                                    error = result.error.message, loading = false
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

    fun consume() {
        _articles.update { homeUiState ->
            homeUiState.copy(error = null)
        }
    }
}

@Parcelize
data class ItemArticleUiState(
    val article: HeadlineArticle, val onClick: (HeadlineArticle) -> Unit
) : Parcelable

data class HomeUiState(
    val data: List<ItemArticleUiState>? = null,
    val error: String? = null,
    val loading: Boolean? = null
)

