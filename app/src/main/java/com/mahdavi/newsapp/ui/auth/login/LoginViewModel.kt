package com.mahdavi.newsapp.ui.auth.login

import androidx.lifecycle.*
import com.mahdavi.newsapp.data.repository.user.UserRepository
import com.mahdavi.newsapp.di.IoDispatcher
import com.mahdavi.newsapp.ui.auth.register.RegisterResult
import com.mahdavi.newsapp.utils.validate.Validate
import com.mahdavi.newsapp.utils.validate.ValidateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val validator: Validate
) : ViewModel() {

    private var _loginUiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _loginUiState.update { loginUiState ->
            loginUiState.copy(
                loginResult = LoginResult(
                    false,
                    exception.message ?: "Something went wrong!"
                )
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(exceptionHandler) {
            userRepository.signInWithEmailAndPassword(email, password)
            _loginUiState.update { loginUiState ->
                loginUiState.copy(loginResult = LoginResult(true, null))
            }
        }
    }

    fun validateInputs(usernameFlow: Flow<String>, passwordFlow: Flow<String>) {
        usernameFlow
            .combine(passwordFlow) { username, password ->
                username to password
            }
            .onEach {
                _loginUiState.update { loginUiState ->
                    val usernameValidationResult = validator.validateEmail(it.first)
                    val passwordValidationResult = validator.validatePassword(it.second)
                    loginUiState.copy(
                        areInputsValid = it.first.isNotEmpty() && it.second.isNotEmpty() && usernameValidationResult.isSuccess && passwordValidationResult.isSuccess,
                        emailInvalidationResult = if (it.first.isNotEmpty()) usernameValidationResult else null,
                        passwordInvalidationResult = if (it.second.isNotEmpty()) passwordValidationResult else null
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun consumeEmailInvalidationResult() {
        _loginUiState.update { loginUiState ->
            loginUiState.copy(emailInvalidationResult = null)
        }
    }

    fun consumePasswordInvalidationResult() {
        _loginUiState.update { loginUiState ->
            loginUiState.copy(passwordInvalidationResult = null)
        }
    }

    fun consumeLoginResult() {
        _loginUiState.update { loginUiState ->
            loginUiState.copy(loginResult = null)
        }
    }

    fun consumeAreInputsValid() {
        _loginUiState.update { loginUiState ->
            loginUiState.copy(areInputsValid = null)
        }
    }

    data class LoginUiState(
        val loginResult: LoginResult? = null,
        val areInputsValid: Boolean? = null,
        val emailInvalidationResult: ValidateResult? = null,
        val passwordInvalidationResult: ValidateResult? = null,
    )

    data class LoginResult(val isLoggedIn: Boolean? = null, val errorMessage: String? = null)
}