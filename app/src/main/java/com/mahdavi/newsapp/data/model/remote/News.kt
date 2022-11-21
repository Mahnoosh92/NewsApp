package com.mahdavi.newsapp.data.model.remote

import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("articles") val articles: List<ArticleResponse?>?,
    @SerializedName("page") val page: Int?,
    @SerializedName("page_size")  val pageSize: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("total_hits") val totalHits: Int?,
    @SerializedName("total_pages") val totalPages: Int?,
    @SerializedName("user_input") val userInput: UserInput?
)