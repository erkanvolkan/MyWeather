package com.my.weatther.viewmodels

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.weatther.LocationHelper
import com.my.weatther.Utils
import com.my.weatther.models.WeatherResponse
import com.my.weatther.repo.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepo, // Repository for weather data
    @ApplicationContext private val context: Context // Application context for loading saved city name
) : ViewModel() {

    // UI state for WeatherScreen, initialized to INITIAL
    private val _uiState = MutableStateFlow<UiStates>(UiStates.INITIAL)
    val uiState: StateFlow<UiStates> = _uiState

    // Load saved city name and fetch weather data on initialization
    init {
        Utils.loadSavedCityName(context)?.let {
            getWeather(it)
        }
    }

    // Fetch weather data for the specified city and update UI state
    fun getWeather(city: String) {
        viewModelScope.launch {
            _uiState.value = UiStates.LOADING // Set state to LOADING

            try {
                weatherRepository.getData(query = "$city,US").collectLatest {
                    _uiState.value = UiStates.SUCCESS(it) // Update state on success
                }

            } catch (e: Exception) {
                _uiState.value = UiStates.ERROR(e.message ?: "Unknown error") // Handle errors
            }
        }
    }

    // Sealed class representing different UI states
    sealed class UiStates {
        data object INITIAL : UiStates() // Initial state
        data object LOADING : UiStates() // Loading state
        data class SUCCESS(val weatherResponse: WeatherResponse) : UiStates() // Success state with data
        data class ERROR(val error: String) : UiStates() // Error state with message
    }
}
