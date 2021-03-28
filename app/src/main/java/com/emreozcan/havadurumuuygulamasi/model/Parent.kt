package com.emreozcan.havadurumuuygulamasi.model


import com.google.gson.annotations.SerializedName

/**
 * Günlük Gelen Hava Durumunun Parent Classıdır
 */
data class Parent(
    @SerializedName("latt_long")
    val lattLong: String,
    @SerializedName("location_type")
    val locationType: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("woeid")
    val woeid: Int
)