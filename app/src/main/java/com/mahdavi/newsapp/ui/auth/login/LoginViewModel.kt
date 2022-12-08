package com.mahdavi.newsapp.ui.auth.login

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdavi.newsapp.data.repository.auth.AuthRepository
import com.mahdavi.newsapp.di.IoDispatcher
import com.mahdavi.newsapp.utils.PASSWORD
import com.mahdavi.newsapp.utils.USERNAME
import com.mahdavi.newsapp.utils.validate.Validate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val validator: Validate
) : ViewModel() {

    private var _loginState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val loginState = _loginState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch() {
            getUser(USERNAME, PASSWORD)
                .map {
                    it
                }
                .collectLatest {
                    val x = it
                }
//            authRepository.getUser(USERNAME, PASSWORD)
//                .collectLatest {
//                    it?.let {
//                        if (it.username.isNullOrEmpty().not() && it.password.isNullOrEmpty()
//                                .not()
//                        ) {
//                            if (it.username == username && it.password == password) {
//                                _loginState.update { loginUiState ->
//                                    loginUiState.copy(isLoggedIn = true)
//                                }
//                            } else {
//                                _loginState.update { loginUiState ->
//                                    loginUiState.copy(
//                                        isLoggedIn = false,
//                                        error = "some thing is wrong!"
//                                    )
//                                }
//                            }
//                        }
//                    } ?: kotlin.run {
//                        authRepository.loginUser(
//                            usernameKey = USERNAME,
//                            usernameValue = username,
//                            passwordKey = PASSWORD,
//                            passwordValue = password
//                        ).collectLatest {
//                            _loginState.update { loginUiState ->
//                                loginUiState.copy(isLoggedIn = true)
//                            }
//                        }
//
//                    }
//                }
        }

    }

    fun getUser(usernameKey: String, passwordKey: String) =
        authRepository.getUser(usernameKey, passwordKey)

}

data class LoginUiState(val isLoggedIn: Boolean? = null, val error: String? = null)