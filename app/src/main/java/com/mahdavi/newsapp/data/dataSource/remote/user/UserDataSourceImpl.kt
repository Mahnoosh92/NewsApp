package com.mahdavi.newsapp.data.dataSource.remote.user

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.disposeOnCancellation
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserDataSourceImpl @Inject constructor() : UserDataSource {
    private val auth: FirebaseAuth = Firebase.auth

    override fun getCurrentUser() = flow{
        emit(auth.currentUser)
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String, password: String
    ): AuthResult = suspendCancellableCoroutine { continuation ->
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result)
                } else {
                    continuation.resumeWithException(
                        task.exception ?: Exception("Something went wrong!")
                    )
                }
            }
        continuation.invokeOnCancellation {

        }
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult =
        suspendCancellableCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result)
                } else {
                    continuation.resumeWithException(
                        task.exception ?: Exception("Something went wrong!")
                    )
                }
                continuation.invokeOnCancellation {

                }
            }
        }

    override fun signOut() = flow {
        auth.signOut()
        emit(Unit)
    }
}