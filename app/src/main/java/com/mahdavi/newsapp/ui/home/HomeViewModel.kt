package com.mahdavi.newsapp.ui.home

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.*
import com.mahdavi.newsapp.data.model.local.ResultWrapper.Error
import com.mahdavi.newsapp.data.model.local.ResultWrapper.Value
import com.mahdavi.newsapp.data.model.remote.ArticleResponse
import com.mahdavi.newsapp.data.repository.NewsRepository
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
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _articles = MutableStateFlow(HomeUiState())
    val articles = _articles.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _articles.update {
            it.copy(error = exception.message.toString())
        }
    }

    init {
        getNews()
    }

    private fun getNews(topic: String = "business") {
        viewModelScope.launch(exceptionHandler) {
            newsRepository.getNews(topic)
                .flowOn(ioDispatcher)
                .onEach { result ->
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
                                    error = result.error.message,
                                    loading = false
                                )
                            }
                        }
                        else -> {
                            throw  IllegalArgumentException("wrong args")
                        }
                    }
                }.catch { error ->
                    Timber.e(error)

                }
                .collect()
        }
    }
// [1, 2,3,4] [[1], [2], [3], [4]] // https://medium.com/huawei-developers/instant-search-using-kotlin-flow-in-search-kit-a7c60233b881
    fun getQuery(input: StateFlow<String>) {
        input
            .debounce(300L)
            .distinctUntilChanged()
            .map {
                (1..5).asFlow()
            }
            .map { it.collect()
                7
            }
            .map {

            }

    }

}

@Parcelize
data class ItemArticleUiState(
    val article: ArticleResponse,
    val onClick: (ArticleResponse) -> Unit
) : Parcelable

data class HomeUiState(
    val data: List<ItemArticleUiState>? = null,
    val error: String? = null,
    val loading: Boolean = true
)

