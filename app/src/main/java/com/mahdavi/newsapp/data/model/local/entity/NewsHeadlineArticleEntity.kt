package com.mahdavi.newsapp.data.model.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mahdavi.newsapp.data.model.HeadlineArticle

@Entity(tableName = "headline_articles")
data class NewsHeadlineArticleEntity(
    @PrimaryKey(autoGenerate = true) val idTable: Int = 0,
    @ColumnInfo(name = "_id") val id: String?,
    @ColumnInfo(name = "_score") val score: String?,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "authors") val authors: String?,
    @ColumnInfo(name = "clean_url") val cleanUrl: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "excerpt") val excerpt: String?,
    @ColumnInfo(name = "is_opinion") val isOpinion: Boolean?,
    @ColumnInfo(name = "language") val language: String?,
    @ColumnInfo(name = "link") val link: String?,
    @ColumnInfo(name = "media") val media: String?,
    @ColumnInfo(name = "published_date") val publishedDate: String?,
    @ColumnInfo(name = "published_date_precision") val publishedDatePrecision: String?,
    @ColumnInfo(name = "rank") val rank: Int?,
    @ColumnInfo(name = "rights") val rights: String?,
    @ColumnInfo(name = "summary") val summary: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "topic") val topic: String?,
    @ColumnInfo(name = "twitter_account") val twitterAccount: String?
) {

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
