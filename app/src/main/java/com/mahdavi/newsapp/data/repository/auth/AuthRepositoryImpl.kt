package com.mahdavi.newsapp.data.repository.auth

import com.mahdavi.newsapp.data.dataSource.remote.auth.AuthDataSource
import com.mahdavi.newsapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ) = withContext(ioDispatcher) {
        authDataSource.createUserWithEmailAndPassword(email, password)
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) =
        withContext(ioDispatcher) {
            authDataSource.signInWithEmailAndPassword(email, password)
        }

}