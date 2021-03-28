package com.emreozcan.havadurumuuygulamasi.model


import com.google.gson.annotations.SerializedName

/**
 * Şehrin Günlük Hava Durumu Classıdır
 */
data class ConsolidatedWeather(
    @SerializedName("air_pressure")
    val airPressure: Double,
    @SerializedName("applicable_date")
    var applicableDate: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("id")
    val id: Long,
    @SerializedName("max_temp")
    val maxTemp: Double,
    @SerializedName("min_temp")
    val minTemp: Double,
    @SerializedName("predictability")
    val predictability: Int,
    @SerializedName("the_temp")
    val theTemp: Double,
    @SerializedName("visibility")
    val visibility: Double,
    @SerializedName("weather_state_abbr")
    val weatherStateAbbr: String,
    @SerializedName("weather_state_name")
    val weatherStateName: String,
    @SerializedName("wind_direction")
    val windDirection: Double,
    @SerializedName("wind_direction_compass")
    val windDirectionCompass: String,
    @SerializedName("wind_speed")
    val windSpeed: Double
)