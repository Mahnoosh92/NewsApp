package com.mahdavi.newsapp.data.repository.user

import android.net.Uri
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUser(): Flow<ResultWrapper<Exception, User?>>
    suspend fun updateProfile(name: String?, url: Uri?): Flow<Unit>
    suspend fun updateEmail(email: String): Flow<Unit>
    suspend fun sendEmailVerification(): Flow<Unit>
    fun signOut(): Flow<Unit>
}