package com.mahdavi.newsapp.data.repository.user

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUser(): Flow<ResultWrapper<Exception, User?>>
    suspend fun updateProfile(user: UserProfileChangeRequest): Unit
}