package com.mahdavi.newsapp.data.model.remote

import com.google.gson.annotations.SerializedName

data class MyError(
    @SerializedName("status") val status: String?,
    @SerializedName("error_code") val code: String?,
    @SerializedName("message") val message: String?,
)