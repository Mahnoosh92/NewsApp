package com.mahdavi.newsapp.ui.auth.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.repository.user.UserRepository
import com.mahdavi.newsapp.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _splashUiState = MutableStateFlow(SplashUiState())
    val splashUiState = _splashUiState.asStateFlow()

    fun checkUserStatus() {
        viewModelScope.launch {
            delay(1000)
            userRepository.getCurrentUser()
                .map { resultWrapper ->
                    when (resultWrapper) {
                        is ResultWrapper.Value -> {
                            resultWrapper.value?.let {
                                _splashUiState.update { splashUiState ->
                                    splashUiState.copy(isCurrentUserSignedIn = true)
                                }
                            } ?: kotlin.run {
                                _splashUiState.update { splashUiState ->
                                    splashUiState.copy(isCurrentUserSignedIn = false)
                                }
                            }
                        }
                        is ResultWrapper.Error -> {
                            _splashUiState.update { splashUiState ->
                                splashUiState.copy(errorMessage = resultWrapper.error.message)
                            }
                        }
                    }
                }
                .flowOn(ioDispatcher)
                .catch { error ->
                    _splashUiState.update { splashUiState ->
                        splashUiState.copy(errorMessage = error.message)
                    }
                }
                .collect()
        }
    }

    fun consumeIsCurrentUserSignedIn() {
        _splashUiState.update { splashUiState ->
            splashUiState.copy(isCurrentUserSignedIn = null)
        }
    }
}

data class SplashUiState(
    val isLoading: Boolean = true,
    val isCurrentUserSignedIn: Boolean? = null,
    val errorMessage: String? = null
)