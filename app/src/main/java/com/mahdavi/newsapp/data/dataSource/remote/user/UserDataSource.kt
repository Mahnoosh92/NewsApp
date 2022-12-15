package com.mahdavi.newsapp.data.dataSource.remote.user

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun getCurrentUser(): Flow<FirebaseUser?>
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult
    fun signOut(): Flow<Unit>
}