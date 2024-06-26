package com.mahdavi.newsapp.data.model.remote.searchedNews

import com.google.gson.annotations.SerializedName
import com.mahdavi.newsapp.data.model.SearchedArticle
import com.mahdavi.newsapp.data.model.local.entity.SearchedNewsArticleEntity

data class SearchedNewsArticle(
    @SerializedName("_id") val id: String?,
    @SerializedName("_score") val score: Int?,
    @SerializedName("author") val author: String?,
//    @SerializedName("authors") val authors: List<String>?,
    @SerializedName("clean_url") val cleanUrl: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("excerpt") val excerpt: String?,
    @SerializedName("is_opinion") val isOpinion: Boolean?,
    @SerializedName("language") val language: String?,
    @SerializedName("link") val link: String?,
    @SerializedName("media") val media: String?,
    @SerializedName("published_date") val publishedDate: String?,
    @SerializedName("published_date_precision") val publishedDatePrecision: String?,
    @SerializedName("rank") val rank: Int?,
    @SerializedName("rights") val rights: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("topic") val topic: String?,
    @SerializedName("twitter_account") val twitterAccount: String?
) {
    fun toSearchedNewsArticleEntity() = SearchedNewsArticleEntity(
        id = id,
        score = score,
        author = author,
//       authors = authors,
        cleanUrl = cleanUrl,
        country = country,
        excerpt = excerpt,
        isOpinion = isOpinion,
        language = language,
        link = link,
        media = media,
        publishedDate = publishedDate,
        publishedDatePrecision = publishedDatePrecision,
        rank = rank,
        rights = rights,
        summary = summary,
        title = title,
        topic = topic,
        twitterAccount = twitterAccount
    )

    fun toSearchedArticle() = SearchedArticle(
        id = id,
        score = score,
        author = author,
//        authors = authors,
        cleanUrl = cleanUrl,
        country = country,
        excerpt = excerpt,
        isOpinion = isOpinion,
        language = language,
        link = link,
        media = media,
        publishedDate = publishedDate,
        publishedDatePrecision = publishedDatePrecision,
        rank = rank,
        rights = rights,
        summary = summary,
        title = title,
        topic = topic,
        twitterAccount = twitterAccount
    )
}
