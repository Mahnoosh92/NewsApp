package com.mahdavi.newsapp.data.dataSource.remote.user

import android.net.Uri
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.mahdavi.newsapp.data.model.remote.User
import kotlinx.coroutines.disposeOnCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserDataSourceImpl @Inject constructor() : UserDataSource {
    private val auth: FirebaseAuth = Firebase.auth

    override fun getCurrentUser() = channelFlow {
        send(auth.currentUser)
    }

    override suspend fun updateProfile(name: String?, url: Uri?): Unit {
        val request = userProfileChangeRequest {
            displayName = name ?: auth.currentUser?.displayName
            photoUri = url ?: auth.currentUser?.photoUrl
        }
        suspendCancellableCoroutine { continuation ->
            auth.currentUser?.updateProfile(request)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(
                        task.exception ?: Exception("Something went wrong!")
                    )
                }
            }
            continuation.invokeOnCancellation {

            }
        }
    }

    override suspend fun updateEmail(email: String) {
        suspendCancellableCoroutine { continuation ->
            auth.currentUser?.updateEmail(email)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(
                        task.exception ?: Exception("Something went wrong!")
                    )
                }
            }
            continuation.invokeOnCancellation {

            }
        }
    }

    override suspend fun sendEmailVerification() {
        suspendCancellableCoroutine { continuation ->
            auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(
                        task.exception ?: Exception("Something went wrong!")
                    )
                }
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