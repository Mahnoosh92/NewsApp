package com.mahdavi.newsapp.data.dataSource.remote.user


import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun getCurrentUser(): Flow<FirebaseUser?>
    suspend fun updateProfile(name: String?, url: Uri?): Unit
    suspend fun updateEmail(email: String): Unit
    suspend fun sendEmailVerification(): Unit
    fun signOut(): Flow<Unit>
}