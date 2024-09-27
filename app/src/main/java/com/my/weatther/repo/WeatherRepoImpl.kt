package com.my.weatther.repo

import com.my.weatther.models.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepoImpl {

    // Suspended function to fetch weather data for a given query, returning it as a Flow
    suspend fun getData(query: String): Flow<WeatherResponse>
}
