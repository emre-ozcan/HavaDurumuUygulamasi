package com.emreozcan.havadurumuuygulamasi.service


import com.emreozcan.havadurumuuygulamasi.model.ConsolidatedWeather
import com.emreozcan.havadurumuuygulamasi.model.Locations
import com.emreozcan.havadurumuuygulamasi.model.WeatherJson
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Url

/**
 * Service Interface
 */
interface WeatherAPI {
    @GET()
    fun getLocations(@Url url : String): Observable<List<Locations>>

    @GET()
    fun getWeather(@Url url : String): Observable<WeatherJson>

    @GET()
    fun getDay(@Url url : String): Observable<List<ConsolidatedWeather>>


}