package com.mahdavi.newsapp.data.repository.user

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    fun getCurrentUser(): FirebaseUser?
    suspend fun createUserWithEmailAndPassword(email:String, password:String): AuthResult
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult
}