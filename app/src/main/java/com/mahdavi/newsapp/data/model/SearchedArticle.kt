package com.mahdavi.newsapp.data.model


data class SearchedArticle(
    val id: String?,
    val score: Int?,
    val author: String?,
//    val authors: List<String>?,
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
)

