package com.mahdavi.newsapp.data.repository.user

import com.google.firebase.auth.AuthResult
import com.mahdavi.newsapp.data.dataSource.remote.user.UserDataSource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDataSource: UserDataSource) :
    UserRepository {
    override fun getCurrentUser() = userDataSource.getCurrentUser()

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ) = userDataSource.createUserWithEmailAndPassword(email, password)

    override suspend fun signInWithEmailAndPassword(email: String, password: String) = userDataSource.signInWithEmailAndPassword(email, password)
}