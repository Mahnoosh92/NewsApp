package com.mahdavi.newsapp.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.User
import com.mahdavi.newsapp.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

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

    fun updateProfileInformation() {


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
    val isClosed: Boolean? = null
)