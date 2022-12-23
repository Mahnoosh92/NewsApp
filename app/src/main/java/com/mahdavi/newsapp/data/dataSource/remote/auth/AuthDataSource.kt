package com.mahdavi.newsapp.data.dataSource.remote.auth

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult
    fun signOut(): Flow<Unit>
}