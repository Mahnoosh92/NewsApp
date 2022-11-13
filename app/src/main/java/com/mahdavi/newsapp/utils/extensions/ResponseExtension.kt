package com.mahdavi.newsapp.utils.extensions

import com.google.gson.Gson
import com.mahdavi.newsapp.data.model.remote.MyError

import retrofit2.Response

fun Response<*>.getApiError(): MyError? {
    return try {
        val errorJsonString = this.errorBody()?.string()
        Gson().fromJson(errorJsonString, MyError::class.java)
    } catch (exception: Exception) {
        exception.printStackTrace()
        null
    }
}
