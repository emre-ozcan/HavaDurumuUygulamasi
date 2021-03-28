package com.emreozcan.havadurumuuygulamasi.model

import com.google.gson.annotations.SerializedName

/**
 * Lokason Classıdır
 */
data class Locations (
    val distance : Int,
    val title : String,
    val location_type : String,
    val woeid : Int,
    val latt_long : String
        )






