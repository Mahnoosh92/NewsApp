package com.mahdavi.newsapp.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdavi.newsapp.data.repository.onboarding.OnBoardingRepository
import com.mahdavi.newsapp.ui.auth.login.LoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val onBoardingRepository: OnBoardingRepository) :
    ViewModel() {

    private var _onBoardingUiState = MutableStateFlow(OnBoardingUiState())
    val onBoardingUiState = _onBoardingUiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _onBoardingUiState.update { onBoardingUiState ->
            onBoardingUiState.copy(error = exception.message)
        }
    }

    fun consumeOnBoarding() {
        onBoardingRepository.onBoardingConsumed()
            .onEach {
                _onBoardingUiState.update { onBoardingUiState ->
                    onBoardingUiState.copy(isConsumed = true)
                }
            }
            .catch { error ->
                _onBoardingUiState.update { onBoardingUiState ->
                    onBoardingUiState.copy(error = error.message)
                }
            }
            .launchIn(viewModelScope)
    }
}

data class OnBoardingUiState(
     val isConsumed: Boolean? = null,
     val error: String? = null
)