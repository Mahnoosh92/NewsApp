package com.mahdavi.newsapp.data.model.remote

import com.google.gson.annotations.SerializedName

data class SourceRemote(
    @SerializedName("message") private val message: String,
    @SerializedName("sources") private val sources: List<String>,
    @SerializedName("user_input") private val userInput: UserInput
)
