package com.mahdavi.newsapp.data.model.remote.news

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HeadlineArticle(
    val id: String?,
    val score: String?,
    val author: String?,
    val authors: String?,
    val cleanUrl: String?,
    val country: String?,
    val excerpt: String?,
    val isOpinion: Boolean?,
    val language: String?,
    val link: String?,
    val media: String?,
    val publishedDate: String?,
    val publishedDatePrecision: String?,
    val rank: Int?,
    val rights: String?,
    val summary: String?,
    val title: String?,
    val topic: String?,
    val twitterAccount: String?
): Parcelable
