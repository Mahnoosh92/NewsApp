package com.mahdavi.newsapp.data.model.remote

import com.google.gson.annotations.SerializedName
import com.mahdavi.newsapp.data.model.local.entity.Article

data class News(
    @SerializedName("status") val status: String?,
    @SerializedName("totalResults") val totalResults: Int?,
    @SerializedName("articles") val articles: List<ArticleResponse?>?
)

data class ArticleResponse(
    @SerializedName("source") val source: Source?,
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("urlToImage") val urlToImage: String?,
    @SerializedName("publishedAt") val publishedAt: String?,
    @SerializedName("content") val content: String?
) {

    fun toArticle()= Article(
            author = author,
            description = description,
            title = title,
            url = url,
            urlToImage = urlToImage,
            publishedAt = publishedAt,
            content = content
        )

}

data class Source(@SerializedName("Id") val id: String?, @SerializedName("Name") val name: String?)