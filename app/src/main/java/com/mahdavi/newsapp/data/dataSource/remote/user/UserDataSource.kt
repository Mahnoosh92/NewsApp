package com.mahdavi.newsapp.data.dataSource.remote.user

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun getCurrentUser(): Flow<FirebaseUser?>
}