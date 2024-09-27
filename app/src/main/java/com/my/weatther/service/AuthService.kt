package com.my.weatther.service

import com.my.weatther.Utils
import com.my.weatther.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthService {

    // Fetches weather data for a given query, with optional unit and API key parameters
    @GET("data/2.5/weather")
    suspend fun getWeatherData(
        @Query("q") query: String,               // City name to search for
        @Query("units") unit: String = "metric", // Measurement unit (default: metric)
        @Query("appid") appId: String = Utils.API_KEY, // API key for authentication
    ): WeatherResponse
}
