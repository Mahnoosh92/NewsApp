package com.mahdavi.newsapp.data.dataSource.remote.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthDataSourceImpl @Inject constructor() : AuthDataSource {

    private val auth: FirebaseAuth = Firebase.auth

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
}