package com.mahdavi.newsapp.data.repository.auth

import com.mahdavi.newsapp.data.dataSource.local.datastore.AuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authDataSource: AuthDataSource) :
    AuthRepository {
    override suspend fun loginUser(
        usernameValue: String,
        passwordValue: String
    ) = authDataSource.loginUser(usernameValue, passwordValue)

    override fun getUser() =
        authDataSource.getUser()
}