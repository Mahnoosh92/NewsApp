package com.mahdavi.newsapp.data.repository.user

import android.net.Uri
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
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

    override fun getCurrentUser(): Flow<ResultWrapper<Exception, User?>> = channelFlow {
        userDataSource.getCurrentUser()
            .map(FirebaseUser?::toUser)
            .onEach {
                send(ResultWrapper.build { it })
            }
            .flowOn(ioDispatcher)
            .catch { e ->
                send(ResultWrapper.build { throw e })
            }
            .collect()
    }

    override suspend fun updateProfile(name: String?, url: Uri?) = flow {
        userDataSource.updateProfile(name, url)
        emit(Unit)
    }

    override fun signOut() = userDataSource.signOut()


}