package com.mahdavi.newsapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.news.HeadlineArticle
import com.mahdavi.newsapp.data.repository.favourite.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val favouriteRepository: FavouriteRepository) :
    ViewModel() {

    private var _favoriteUiState = MutableStateFlow(FavoriteUiState())
    val favoriteUiState = _favoriteUiState.asStateFlow()

    fun loadHeadlines(topic: String = "news") {
        favouriteRepository.getLatestHeadlines(topic)
            .onEach { result ->
                when (result) {
                    is ResultWrapper.Value -> {
                        _favoriteUiState.update { favoriteUiState ->
                            favoriteUiState.copy(headlines = result.value)
                        }
                    }
                    is ResultWrapper.Error -> {
                        _favoriteUiState.update { favoriteUiState ->
                            favoriteUiState.copy(error = result.error.message)
                        }
                    }
                }
            }
            .catch { error ->
                _favoriteUiState.update { favoriteUiState ->
                    favoriteUiState.copy(error = error.message)
                }
            }
            .launchIn(viewModelScope)
    }

    fun loadFavouriteHeadlines() {
        favouriteRepository.getFavouriteHeadlines()
            .onEach { result ->
                when (result) {
                    is ResultWrapper.Value -> {
                        _favoriteUiState.update { favoriteUiState ->
                            favoriteUiState.copy(favouriteHeadLines = result.value)
                        }
                    }
                    is ResultWrapper.Error -> {
                        _favoriteUiState.update { favoriteUiState ->
                            favoriteUiState.copy(error = result.error.message)
                        }
                    }
                }
            }
            .catch { error ->
                _favoriteUiState.update { favoriteUiState ->
                    favoriteUiState.copy(error = error.message)
                }
            }
            .launchIn(viewModelScope)
    }

    fun addToFavouriteHeadlines(headline: HeadlineArticle) {
        favouriteRepository.updateFavouriteHeadlines(headline)
            .catch { error ->
                _favoriteUiState.update { favoriteUiState ->
                    favoriteUiState.copy(error = error.message)
                }
            }
            .launchIn(viewModelScope)
    }
}

data class FavoriteUiState(
    val headlines: List<HeadlineArticle>? = null,
    val favouriteHeadLines: List<HeadlineArticle>? = null,
    val error: String? = null
)