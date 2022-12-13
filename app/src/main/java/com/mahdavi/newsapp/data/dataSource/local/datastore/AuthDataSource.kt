package com.mahdavi.newsapp.data.dataSource.local.datastore

import com.mahdavi.newsapp.data.model.local.auth.User
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    suspend fun loginUser(
        usernameValue: String,
        passwordValue: String
    ): Flow<Unit>

    fun getUser(): Flow<User?>
}