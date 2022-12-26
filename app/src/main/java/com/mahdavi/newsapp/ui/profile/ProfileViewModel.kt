package com.mahdavi.newsapp.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.User
import com.mahdavi.newsapp.data.repository.user.UserRepository
import com.mahdavi.newsapp.di.IoDispatcher
import com.mahdavi.newsapp.ui.auth.login.LoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) :
    ViewModel() {

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _profileUiState.update { profileUiState ->
            profileUiState.copy(
                user = null,
                error = exception.message,
                isUpdatedSuccessfully = false,
                isLoadingPhotoUri = null
            )
        }
    }

    fun loadDataToProfileForm() {
        userRepository.getCurrentUser()
            .onEach { result ->
                when (result) {
                    is ResultWrapper.Value -> {
                        _profileUiState.update { profileUiState ->
                            profileUiState.copy(user = result.value)
                        }
                    }
                    is ResultWrapper.Error -> {
                        _profileUiState.update { profileUiState ->
                            profileUiState.copy(error = result.error.message)
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateLoadingStatus(state: Boolean?) {
        _profileUiState.update { profileUiState ->
            profileUiState.copy(
                isLoadingPhotoUri = state
            )
        }
    }

    fun updateProfileInformation(name: String?, uri: Uri?) {
        viewModelScope.launch(exceptionHandler) {
            userRepository.updateProfile(name, uri)
                .onCompletion {
                    updateLoadingStatus(false)
                }
                .flowOn(ioDispatcher)
                .catch { error ->
                    _profileUiState.update { profileUiState ->
                        profileUiState.copy(
                            user = null,
                            error = error.message,
                            isUpdatedSuccessfully = false,
                            isLoadingPhotoUri = null
                        )
                    }
                }
                .collect {
                    _profileUiState.update { profileUiState ->
                        profileUiState.copy(isUpdatedSuccessfully = true)
                    }
                }
        }
    }

    fun consumeUpdatingStatus() {
        _profileUiState.update { profileUiState ->
            profileUiState.copy(isUpdatedSuccessfully = null)
        }
    }

    fun setImageUri(uri: Uri) {
        _profileUiState.update { profileUiState ->
            val user = profileUiState.user?.copy(photoUrl = uri)
            profileUiState.copy(user = user)
        }
    }
}

data class ProfileUiState(
    val user: User? = null,
    val error: String? = null,
    val isUpdatedSuccessfully: Boolean? = null,
    val isLoadingPhotoUri: Boolean? = null
)