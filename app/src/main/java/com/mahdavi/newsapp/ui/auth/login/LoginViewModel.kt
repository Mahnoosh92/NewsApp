package com.mahdavi.newsapp.ui.auth.login

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.data.repository.auth.AuthRepository
import com.mahdavi.newsapp.di.IoDispatcher
import com.mahdavi.newsapp.utils.validate.Validate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _loginState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val loginState = _loginState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {

        }
    }

    fun validateLoginInputs(usernameFlow: Flow<String>, passwordFlow: Flow<String>) {
        usernameFlow
            .combine(passwordFlow) { username, password ->
                username to password
            }
            .onEach {
                _loginState.update { loginUiState ->
                    loginUiState.copy(areInputsValid = it.first.isNotEmpty() && it.second.isNotEmpty())
                }
            }
            .launchIn(viewModelScope)
    }

    data class LoginUiState(
        val isLoggedIn: Boolean? = null,
        @StringRes val error: Int = -1,
        val areInputsValid: Boolean = false
    )
}