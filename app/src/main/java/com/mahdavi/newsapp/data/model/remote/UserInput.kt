package com.mahdavi.newsapp.data.model.remote

import com.google.gson.annotations.SerializedName

data class UserInput(
    @SerializedName("countries") val countries: List<String?>?,
    @SerializedName("from") val from: String?,
    @SerializedName("lang") val lang: String?,
    @SerializedName("not_countries") val notCountries: String?,
    @SerializedName("not_lang") val notLang: String?,
    @SerializedName("not_sources") val notSources: String?,
    @SerializedName("page") val page: Int?,
    @SerializedName("size") val size: Int?,
    @SerializedName("sources") val sources: String?,
    @SerializedName("topic") val topic: String?
)