package com.mahdavi.newsapp.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.mahdavi.newsapp.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private var _settingUiState = MutableStateFlow(SettingUiState())
    val settingUiState = _settingUiState.asStateFlow()

    fun signOut() {
        viewModelScope.launch {
            userRepository.signOut()
                .catch { error->
                    _settingUiState.update { settingUiState ->
                        settingUiState.copy(error = error.message, isSignedOut = false)
                    }
                }
                .collect{
                    _settingUiState.update { settingUiState ->
                        settingUiState.copy( isSignedOut = true)
                    }
                }
        }
    }
}

data class SettingUiState(val isSignedOut: Boolean? = null, val error: String? = null)