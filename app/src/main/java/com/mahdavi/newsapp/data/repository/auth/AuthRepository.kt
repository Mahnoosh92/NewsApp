package com.mahdavi.newsapp.data.repository.auth

import com.google.firebase.auth.AuthResult
import com.mahdavi.newsapp.data.model.local.ResultWrapper
import com.mahdavi.newsapp.data.model.remote.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun createUserWithEmailAndPassword(email:String, password:String): AuthResult
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult
}