package com.mahdavi.newsapp.data.dataSource.local.auth

import com.mahdavi.newsapp.data.model.local.auth.User
import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {
    suspend fun loginUser(
        usernameKey: String,
        usernameValue: String,
        passwordKey: String,
        passwordValue: String
    ): Flow<Unit>

    fun getUser(usernameKey: String, passwordKey: String): Flow<User?>
}