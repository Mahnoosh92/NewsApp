package com.mahdavi.newsapp.data.dataSource.local.auth

import com.mahdavi.newsapp.data.model.local.auth.User
import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {
    suspend fun loginUser(
        usernameValue: String,
        passwordValue: String
    ): Flow<Unit>

    fun getUser(): Flow<User?>
}