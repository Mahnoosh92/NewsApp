package com.mahdavi.newsapp.ui.headlines

import androidx.lifecycle.*
import com.mahdavi.newsapp.data.model.local.HeadlineTitle
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.news.HeadlineArticle
import com.mahdavi.newsapp.data.repository.news.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HeadlineViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _articles = MutableStateFlow(HeadlineUiState())
    val articles = _articles.asStateFlow()


    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _articles.update {
            it.copy(error = exception.message.toString())
        }
    }

    fun updateTitles() {
        viewModelScope.launch {
            newsRepository.getHeadlinesTitle()
                .map { titles ->
                    _articles.update {
                        it.copy(titles = titles)
                    }
                    titles
                }
                .flatMapLatest { list ->
                    newsRepository.getLatestHeadlines(list.filter{it.isSelected}.get(0).title)
                }
                .catch { }
                .collect{result->
                    when (result) {
                        is ResultWrapper.Value -> {
                            _articles.update { headlineUiState ->
                                headlineUiState.copy(data = result.value)
                            }
                        }
                        is ResultWrapper.Error -> {
                            _articles.update { headlineUiState ->
                                headlineUiState.copy(error = result.error.message)
                            }
                        }
                        else -> {}
                    }
                }
        }
    }

    fun updateListTitles(headlineTitle: HeadlineTitle) {
        _articles.update { headlineUiState ->
            val titles = headlineUiState.titles?.map { headlineT ->
                if (headlineT.title == headlineTitle.title) {
                    headlineT.copy(isSelected = true)
                } else {
                    headlineT.copy(isSelected = false)
                }
            }
            createHeadlines(titles?.filter{it.isSelected})
            headlineUiState.copy(titles = titles)
        }
    }

    private fun createHeadlines(headlineTitle: List<HeadlineTitle>?) {
        headlineTitle?.let{
            viewModelScope.launch(exceptionHandler) {
                newsRepository.getLatestHeadlines(it.get(0).title)
                    .map { result ->
                        when (result) {
                            is ResultWrapper.Value -> {
                                _articles.update { headlineUiState ->
                                    headlineUiState.copy(data = result.value)
                                }
                            }
                            is ResultWrapper.Error -> {
                                _articles.update { headlineUiState ->
                                    headlineUiState.copy(error = result.error.message)
                                }
                            }
                            else -> {}
                        }
                    }
                    .catch { error -> Timber.e(error) }
                    .collect()
            }
        }
    }

}

data class HeadlineUiState(
    val data: List<HeadlineArticle?>? = null,
    val titles: List<HeadlineTitle>? = null,
    val error: String? = null,
    val loading: Boolean? = null
)




