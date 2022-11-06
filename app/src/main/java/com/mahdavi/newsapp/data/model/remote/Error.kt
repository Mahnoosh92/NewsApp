package com.mahdavi.newsapp.data.model.remote

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("status") val status: String?,
    @SerializedName("code") val code: String?,
    @SerializedName("message") val message: String?,
)