package com.mahdavi.newsapp.data.dataSource.remote.user

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface UserDataSource {
    fun getCurrentUser(): FirebaseUser?
    suspend fun createUserWithEmailAndPassword(email:String, password:String): AuthResult
}