package com.mahdavi.newsapp.data.dataSource.remote.user


import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun getCurrentUser(): Flow<FirebaseUser?>
    suspend fun updateProfile(user: UserProfileChangeRequest): Unit
}