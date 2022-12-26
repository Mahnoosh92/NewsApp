package com.mahdavi.newsapp.ui.auth.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.repository.onboarding.OnBoardingRepository
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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val onBoardingRepository: OnBoardingRepository
) : ViewModel() {

    private var _splashUiState = MutableStateFlow(SplashUiState())
    val splashUiState = _splashUiState.asStateFlow()

    fun checkUserAndOnBoardingStatus() {
        viewModelScope.launch {
            userRepository.getCurrentUser()
                .map { result ->
                    when (result) {
                        is ResultWrapper.Value -> {
                            result.value?.let {
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
                                splashUiState.copy(errorMessage = result.error.message)
                            }
                        }
                    }
                }
                .flatMapConcat {
                    onBoardingRepository.needToShowOnBoarding()
                }
                .map { onBoardingStatus ->
                    _splashUiState.update { splashUiState ->
                        splashUiState.copy(isOnBoardingRequired = onBoardingStatus)
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

    fun consumeIsOnBoardingRequired() {
        _splashUiState.update { splashUiState ->
            splashUiState.copy(isOnBoardingRequired = null)
        }
    }
    fun signOut() {
        userRepository.signOut()
    }
}

data class SplashUiState(
    val isLoading: Boolean = true,
    val isCurrentUserSignedIn: Boolean? = null,
    val isOnBoardingRequired: Boolean? = null,
    val errorMessage: String? = null
)