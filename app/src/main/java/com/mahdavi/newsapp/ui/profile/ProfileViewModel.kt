package com.mahdavi.newsapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdavi.newsapp.data.repository.user.UserRepository
import com.mahdavi.newsapp.ui.auth.login.LoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _profileUiState.update { profileUiState ->
            profileUiState.copy(errorMessage = exception.message)
        }
    }

    fun signOut() {
        viewModelScope.launch(exceptionHandler) {
            userRepository.signOut()
                .onEach {
                    _profileUiState.update { profileUiState ->
                        profileUiState.copy(isSignedIn = false)
                    }
                }
                .catch { error ->
                    _profileUiState.update { profileUiState ->
                        profileUiState.copy(errorMessage = error.message)
                    }
                }
                .collect()
        }
    }
}

data class ProfileUiState(val isSignedIn: Boolean? = null, val errorMessage: String? = null)