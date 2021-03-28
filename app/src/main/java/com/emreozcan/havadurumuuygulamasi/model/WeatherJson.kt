package com.emreozcan.havadurumuuygulamasi.model


import com.emreozcan.havadurumuuygulamasi.model.ConsolidatedWeather
import com.emreozcan.havadurumuuygulamasi.model.Parent
import com.emreozcan.havadurumuuygulamasi.model.Source
import com.google.gson.annotations.SerializedName

/**
 * Toplamda 8 Günlük Hava Durumu Bilgisi İstendiğinden O 7. ve 8.
 * Gün İçin Oluşturulan Class'dır
 */
data class WeatherJson(
    @SerializedName("consolidated_weather")
    val consolidatedWeather: List<ConsolidatedWeather>,
    @SerializedName("latt_long")
    val lattLong: String,
    @SerializedName("location_type")
    val locationType: String,
    @SerializedName("parent")
    val parent: Parent,
    @SerializedName("sources")
    val sources: List<Source>,
    @SerializedName("sun_rise")
    val sunRise: String,
    @SerializedName("sun_set")
    val sunSet: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("timezone_name")
    val timezoneName: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("woeid")
    val woeid: Int
)