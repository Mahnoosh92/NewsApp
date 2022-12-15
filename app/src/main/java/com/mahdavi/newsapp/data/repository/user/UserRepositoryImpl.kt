package com.mahdavi.newsapp.data.repository.user

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.mahdavi.newsapp.data.dataSource.remote.user.UserDataSource
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.User
import com.mahdavi.newsapp.data.model.remote.toUser
import com.mahdavi.newsapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    override fun getCurrentUser(): Flow<ResultWrapper<Exception, User?>> = flow {
        userDataSource.getCurrentUser()
            .map(FirebaseUser?::toUser)
            .onEach {
                emit(ResultWrapper.build { it })
            }
            .flowOn(ioDispatcher)
            .catch { e ->
                emit(ResultWrapper.build { throw e })
            }
            .collect()
    }


    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ) = withContext(ioDispatcher) {
        userDataSource.createUserWithEmailAndPassword(email, password)
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) =
        withContext(ioDispatcher) {
            userDataSource.signInWithEmailAndPassword(email, password)
        }

    override fun signOut() = userDataSource.signOut()
}