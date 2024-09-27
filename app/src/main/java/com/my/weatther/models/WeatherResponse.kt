package com.my.weatther.models

// Represents the overall weather response from the API
data class WeatherResponse(
    val coord: Coord?,         // Coordinates (longitude and latitude)
    val weather: List<Weather>?, // List of weather conditions
    val base: String?,         // Internal parameter for API response
    val main: Main?,           // Main weather information (temperature, pressure, etc.)
    val visibility: Int?,      // Visibility in meters
    val wind: Wind?,           // Wind information (speed and direction)
    val clouds: Clouds?,       // Cloud coverage percentage
    val dt: Long?,             // Timestamp of the data
    val sys: Sys?,             // System information (country, sunrise, sunset)
    val timezone: Int?,        // Timezone offset from UTC
    val id: Int?,              // City ID
    val name: String?,         // City name
    val cod: Int?              // Response code
)

// Represents geographical coordinates
data class Coord(
    val lon: Double,           // Longitude
    val lat: Double            // Latitude
)

// Represents weather conditions
data class Weather(
    val id: Int,               // Weather condition ID
    val main: String,          // Main weather description (e.g., Rain)
    val description: String,   // Detailed weather description
    val icon: String           // Weather icon identifier
)

// Represents main weather parameters
data class Main(
    val temp: Double,          // Current temperature
    val feels_like: Double,    // Temperature perceived by humans
    val temp_min: Double,      // Minimum temperature
    val temp_max: Double,      // Maximum temperature
    val pressure: Int,         // Atmospheric pressure
    val humidity: Int,         // Humidity percentage
    val sea_level: Int?,       // Atmospheric pressure at sea level (optional)
    val grnd_level: Int?       // Atmospheric pressure at ground level (optional)
)

// Represents wind information
data class Wind(
    val speed: Double,         // Wind speed
    val deg: Int               // Wind direction (in degrees)
)

// Represents cloud coverage
data class Clouds(
    val all: Int               // Cloud coverage percentage
)

// Represents system information about the response
data class Sys(
    val type: Int,             // Type of system (e.g., 1 = country)
    val id: Int,               // System ID
    val country: String,       // Country code
    val sunrise: Long,         // Sunrise time (UNIX timestamp)
    val sunset: Long           // Sunset time (UNIX timestamp)
)
