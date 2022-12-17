package com.mahdavi.newsapp.data.model.remote.newsHeadline

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mahdavi.newsapp.data.model.HeadlineArticle
import com.mahdavi.newsapp.data.model.local.entity.NewsHeadlineArticleEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsHeadlineArticle(
    @SerializedName("_id") val id: String?,
    @SerializedName("_score") val score: String?,
    @SerializedName("author") val author: String?,
    @SerializedName("authors") val authors: String?,
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
) : Parcelable {

    fun toNewsHeadlineArticleEntity() = NewsHeadlineArticleEntity(
        id = id,
        score = score,
        author = author,
        authors = authors,
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

    fun toHeadlineArticle() = HeadlineArticle(
        id = id,
        score = score,
        author = author,
        authors = authors,
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