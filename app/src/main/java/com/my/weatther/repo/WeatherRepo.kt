package com.my.weatther.repo

import com.my.weatther.models.WeatherResponse
import com.my.weatther.service.AuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepo @Inject constructor(
    private val authService: AuthService // AuthService for API calls
) : WeatherRepoImpl {

    // Fetches weather data for a given query and emits it as a Flow
    override suspend fun getData(query: String): Flow<WeatherResponse> {
        return flow {
            emit(authService.getWeatherData(query)) // Emit the weather data response
        }
    }
}
