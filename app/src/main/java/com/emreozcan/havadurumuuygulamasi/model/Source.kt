package com.emreozcan.havadurumuuygulamasi.model


import com.google.gson.annotations.SerializedName


/**
 * Günlük Gelen Hava Durumunun Source Classıdır
 */
data class Source(
    @SerializedName("crawl_rate")
    val crawlRate: Int,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)