package com.mahdavi.newsapp.data.dataSource.remote.user

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserDataSourceImpl @Inject constructor() : UserDataSource {
    private var auth: FirebaseAuth = Firebase.auth

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): AuthResult = suspendCoroutine { continuation ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result)
                } else {
                    continuation.resumeWithException(
                        task.exception ?: Exception("Something went wrong!")
                    )
                }
            }
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult =
        suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result)
                } else {
                    continuation.resumeWithException(
                        task.exception ?: Exception("Something went wrong!")
                    )
                }
            }
        }
}