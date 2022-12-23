package com.mahdavi.newsapp.data.dataSource.remote.user

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.disposeOnCancellation
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
}