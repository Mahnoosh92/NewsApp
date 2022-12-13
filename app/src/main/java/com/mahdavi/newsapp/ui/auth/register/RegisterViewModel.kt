package com.mahdavi.newsapp.ui.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdavi.newsapp.data.repository.user.UserRepository
import com.mahdavi.newsapp.di.DefaultDispatcher
import com.mahdavi.newsapp.utils.validate.Validate
import com.mahdavi.newsapp.utils.validate.ValidateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val validator: Validate,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _registrationUiState = MutableStateFlow(RegistrationUiState())
    val registrationUiState = _registrationUiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.i("reza", ": $exception ")
    }

    fun validateRegisterInputs(usernameFlow: Flow<String>, passwordFlow: Flow<String>) {
        usernameFlow
            .combine(passwordFlow) { username, password ->
                username to password
            }
            .onEach {
                _registrationUiState.update { registerUiState ->
                    val usernameValidationResult = validator.validateEmail(it.first)
                    val passwordValidationResult = validator.validatePassword(it.second)
                    registerUiState.copy(
                        areInputsValid = it.first.isNotEmpty()
                                && it.second.isNotEmpty()
                                && usernameValidationResult.isSuccess
                                && passwordValidationResult.isSuccess,
                        emailInvalidationResult = if (it.first.isNotEmpty()) usernameValidationResult else null,
                        passwordInvalidationResult = if (it.second.isNotEmpty()) passwordValidationResult else null
                    )
                }
            }
            .flowOn(defaultDispatcher)
            .catch { error -> Timber.e(error) }
            .launchIn(viewModelScope)
    }

    fun registerUser(email: String, password: String) {
        viewModelScope.launch(exceptionHandler) {
            val user = userRepository.createUserWithEmailAndPassword(email, password).user
            Log.i("Reza", "registerUser: ${user?.email}")
        }
    }
}

data class RegistrationUiState(
    val isLoading: Boolean = false,
    val emailInvalidationResult: ValidateResult? = null,
    val passwordInvalidationResult: ValidateResult? = null,
    val areInputsValid: Boolean = false
)