package com.mahdavi.newsapp.data.repository.auth

import com.mahdavi.newsapp.data.dataSource.local.auth.AuthLocalDataSource
import com.mahdavi.newsapp.data.model.local.auth.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authLocalDataSource: AuthLocalDataSource) :
    AuthRepository {
    override suspend fun loginUser(
        usernameValue: String,
        passwordValue: String
    ) = authLocalDataSource.loginUser(usernameValue, passwordValue)

    override fun getUser() =
        authLocalDataSource.getUser()
}