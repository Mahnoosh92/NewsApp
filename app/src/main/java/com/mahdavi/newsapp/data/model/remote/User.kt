package com.mahdavi.newsapp.data.model.remote

import android.net.Uri
import com.google.firebase.auth.FirebaseUser

data class User(
    val name: String?,
    val email: String?,
    val photoUrl: Uri?,
    val isEmailVerified: Boolean?,
    val uid: String?
)

fun FirebaseUser?.toUser(): User? {
    this?.let {
        return User(
            name = this.displayName,
            email = this.email,
            photoUrl = this.photoUrl,
            isEmailVerified = this.isEmailVerified,
            uid = this.uid
        )
    } ?: kotlin.run { return null }
}