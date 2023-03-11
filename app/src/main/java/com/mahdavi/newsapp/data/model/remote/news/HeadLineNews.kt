package com.mahdavi.newsapp.data.model.remote.news

import com.google.gson.annotations.SerializedName
import com.mahdavi.newsapp.data.model.remote.UserInput

data class HeadLineNews(
    @SerializedName("articles") val articles: List<RemoteHeadlineArticle?>?,
    @SerializedName("page") val page: Int?,
    @SerializedName("page_size")  val pageSize: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("total_hits") val totalHits: Int?,
    @SerializedName("total_pages") val totalPages: Int?,
    //@SerializedName("user_input") val userInput: UserInput?
)
