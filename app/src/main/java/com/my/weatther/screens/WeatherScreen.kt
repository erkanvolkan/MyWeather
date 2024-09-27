package com.my.weatther.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.my.weatther.LocationHelper
import com.my.weatther.R
import com.my.weatther.Utils
import com.my.weatther.models.WeatherResponse
import com.my.weatther.viewmodels.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel() // Provides a ViewModel instance via Hilt Dependency Injection
) {

    val uiState by viewModel.uiState.collectAsState() // Observes the UI state from the ViewModel

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT // Checks whether the device is in portrait mode

    // Define background gradient colors
    val gradientColors = listOf(
        Color(0xFFFBF1A9),
        Color(0xFF53538D),
        Color(0xFF3C3C92)
    )

    // Main screen layout with gradient background
    Column(modifier = Modifier
        .padding(WindowInsets.systemBars.asPaddingValues()) // Adds padding to avoid system bars
        .background(brush = Brush.verticalGradient(colors = gradientColors)) // Background gradient
        .fillMaxSize()) { // Fills the entire available space

        // Search bar section in a box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp) // Set fixed height for the search bar section
                .background(color = Color.DarkGray), // Dark gray background
            contentAlignment = Alignment.Center // Align content to the center
        ) {
            WeatherSearchBar { query -> viewModel.getWeather(query) } // Trigger search with the entered query
        }

        // Main content box that adapts based on the UI state
        Box(
            modifier = Modifier
                .fillMaxSize() // Fills the available space
                .padding(16.dp), // Padding inside the box
            contentAlignment = Alignment.Center // Centers content
        ) {
            // Handle different states like INITIAL, LOADING, SUCCESS, and ERROR
            when (uiState) {
                is WeatherViewModel.UiStates.INITIAL -> {
                    // Display message to prompt user to enter a city
                    Text("Enter a city to get the weather", color = Color.White)
                }
                is WeatherViewModel.UiStates.LOADING -> {
                    // Display loading indicator when data is being fetched
                    CircularProgressIndicator()
                }
                is WeatherViewModel.UiStates.SUCCESS -> {
                    // On success, display weather data in portrait or landscape mode
                    val weatherResponse = (uiState as WeatherViewModel.UiStates.SUCCESS).weatherResponse
                    // LazyColumn allows the screen to scroll
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        item {
                            if(isPortrait){
                                // Show weather content for portrait layout
                                WeatherContentPortrait(weatherResponse = weatherResponse)
                            } else {
                                // Show weather content for landscape layout
                                WeatherContentLandScape(weatherResponse = weatherResponse)
                            }
                        }
                    }
                }
                is WeatherViewModel.UiStates.ERROR -> {
                    // Display error message if there's an issue fetching weather data
                    Text("No Forecast Data", color = Color.White)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearchBar(onSearch: (String) -> Unit) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") } // Holds the current search query

    // Row layout containing the search input field and search button
    Row(
        modifier = Modifier
            .fillMaxWidth() // Search bar takes the full width of the screen
            .padding(16.dp), // Padding around the search bar
        verticalAlignment = Alignment.CenterVertically // Vertically aligns search field and button
    ) {
        // Input text field for entering city name
        TextField(
            value = query,
            onValueChange = { query = it }, // Updates query as user types
            modifier = Modifier
                .weight(1f) // Takes up the remaining space in the row
                .padding(end = 8.dp) // Padding between input field and search button
                .clip(shape = RoundedCornerShape(16.dp)), // Rounded corners for the input field
            placeholder = { Text("Enter city", color = Color.White) }, // Placeholder text inside input field
            singleLine = true, // Ensures input is a single line
            textStyle = TextStyle(fontSize = 18.sp, color = Color.White), // Style for input text
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Gray, // Background color of input field
                cursorColor = Color.White, // Color of the text cursor
                focusedIndicatorColor = Color.Transparent, // No underline when focused
                unfocusedIndicatorColor = Color.Transparent // No underline when unfocused
            )
        )

        // Search button next to the input field
        Box(
            modifier = Modifier
                .size(40.dp) // Fixed size for the button
                .clip(RoundedCornerShape(8.dp)) // Rounded corners for the button
                .background(Color.LightGray) // Light gray background for the button
                .clickable {
                    // Only perform search if query is not blank
                    if (query.isNotBlank()) {
                        Utils.saveCityNameToPreferences(context, cityName = query) // Save the query in preferences
                        onSearch(query) // Trigger the search action
                    }
                },
            contentAlignment = Alignment.Center // Center the search icon inside the button
        ) {
            // Search icon inside the button
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search", // Content description for accessibility
                modifier = Modifier.size(24.dp), // Icon size
                tint = Color.Black // Color of the search icon
            )
        }
    }
}

@Composable
fun WeatherContentPortrait(weatherResponse: WeatherResponse) {
    // Layout in a column for portrait mode
    Column(
        modifier = Modifier
            .fillMaxSize() // Fills the available space
            .padding(16.dp), // Padding inside the column
        horizontalAlignment = Alignment.CenterHorizontally // Aligns content in the center horizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp)) // Adds vertical space

        GreetingBasedOnTime(weatherResponse) // Function to greet the user based on the time of day

        // Displays the weather icon from the weather API
        Image(
            painter = rememberImagePainter("https://openweathermap.org/img/wn/${weatherResponse.weather?.get(0)?.icon}@2x.png"),
            contentDescription = null, // No content description for accessibility
            modifier = Modifier
                .size(220.dp) // Icon size
                .padding(top = 50.dp), // Top padding
            contentScale = ContentScale.Fit // Fits the image within the bounds
        )

        Spacer(modifier = Modifier.height(8.dp)) // Adds vertical space

        // Displays the current temperature and unit (Celsius)
        Row {
            Text(
                text = "${weatherResponse.main?.temp?.toInt()}°", // Temperature value with degree symbol
                style = TextStyle(fontSize = 85.sp, color = Color.White,
                    fontFamily = Utils.poppingBold)
            )

            Text(
                text = "C", // Temperature unit
                style = TextStyle(fontSize = 85.sp, color = Color.White,
                    fontFamily = Utils.poppingBold)
            )
        }

        // Displays the weather description (e.g., "clear sky")
        weatherResponse.weather?.get(0)?.let {
            Text(
                text = it.description, // Description of weather
                style = TextStyle(fontSize = 20.sp, color = Color.White,
                    fontFamily = Utils.poppingBold) // Smaller text for description
            )
        }

        Spacer(modifier = Modifier.height(10.dp)) // Adds vertical space

        // Displays the date and time of the weather data
        Text(
            text = formatTimestamp(weatherResponse.dt!!), // Formats timestamp into a readable format
            style = TextStyle(fontSize = 20.sp, color = Color.White,
                fontFamily = Utils.poppingBold) // Smaller text for date
        )

        Spacer(modifier = Modifier.height(40.dp)) // Adds vertical space

        // First row: Min Temp, Max Temp, Feels Like
        Row(
            modifier = Modifier.fillMaxWidth(), // Fills the entire width
            horizontalArrangement = Arrangement.SpaceEvenly // Spaces elements evenly in the row
        ) {
            WeatherColumn(label = "Min Temp", value = weatherResponse.main?.temp_min!!.toInt().toString() + "°",
                R.drawable.min_temp) // Displays minimum temperature
            WeatherColumn(label = "Max Temp", value = weatherResponse.main.temp_max.toInt().toString() + "°",
                R.drawable.max_temp) // Displays maximum temperature
            WeatherColumn(label = "Feels Like", value = weatherResponse.main.feels_like.toInt().toString() + "°",
                R.drawable.feels_like) // Displays "feels like" temperature
        }

        Spacer(modifier = Modifier.height(16.dp)) // Adds vertical space

        // Second row: Pressure, Humidity, Wind speed
        Row(
            modifier = Modifier.fillMaxWidth(), // Fills the entire width
            horizontalArrangement = Arrangement.SpaceEvenly // Spaces elements evenly in the row
        ) {
            WeatherColumn(label = "Pressure", value = weatherResponse.main?.pressure.toString() + "Pa",
                R.drawable.pressure) // Displays pressure in Pascals
            WeatherColumn(label = "Humidity", value = weatherResponse.main?.humidity.toString() + "g/m3",
                R.drawable.humidity) // Displays humidity
            WeatherColumn(label = "Wind", value = weatherResponse.wind?.speed!!.toInt().toString(),
                R.drawable.wind) // Displays wind speed
        }

        Spacer(modifier = Modifier.height(16.dp)) // Adds vertical space
    }
}

@Composable
fun WeatherContentLandScape(weatherResponse: WeatherResponse) {
    // Layout in a row for landscape mode
    Row(modifier = Modifier.fillMaxWidth()) { // Fills the entire width

        // First column: Greeting and weather icon
        Column(modifier = Modifier.weight(1f)) {
            GreetingBasedOnTime(weatherResponse) // Function to greet the user based on the time of day

            // Displays the weather icon from the weather API
            Image(
                painter = rememberImagePainter("https://openweathermap.org/img/wn/${weatherResponse.weather?.get(0)?.icon}@2x.png"),
                contentDescription = null, // No content description for accessibility
                modifier = Modifier.size(170.dp), // Smaller icon size
                contentScale = ContentScale.Fit // Fits the image within the bounds
            )
        }

        // Second column: Temperature and weather description
        Column(modifier = Modifier.weight(1f)) {
            Row {
                Text(
                    text = "${weatherResponse.main?.temp?.toInt()}°", // Temperature value with degree symbol
                    style = TextStyle(fontSize = 55.sp, color = Color.White,
                        fontFamily = Utils.poppingBold)
                )

                Text(
                    text = "C", // Temperature unit
                    style = TextStyle(fontSize = 55.sp, color = Color.White,
                        fontFamily = Utils.poppingBold)
                )
            }

            // Displays the weather description (e.g., "clear sky")
            weatherResponse.weather?.get(0)?.let {
                Text(
                    text = it.description, // Description of weather
                    style = TextStyle(fontSize = 20.sp, color = Color.White,
                        fontFamily = Utils.poppingBold) // Smaller text for description
                )
            }

            // Displays the date and time of the weather data
            Text(
                text = formatTimestamp(weatherResponse.dt!!), // Formats timestamp into a readable format
                style = TextStyle(fontSize = 20.sp, color = Color.White,
                    fontFamily = Utils.poppingBold) // Smaller text for date
            )
        }

        // Third column: Weather details (Temp, Pressure, Humidity, Wind)
        Column(modifier = Modifier.weight(2f)) {
            // First row: Min Temp, Max Temp, Feels Like
            Row(
                modifier = Modifier.fillMaxWidth(), // Fills the entire width
                horizontalArrangement = Arrangement.SpaceEvenly // Spaces elements evenly in the row
            ) {
                WeatherColumn(label = "Min Temp", value = weatherResponse.main?.temp_min!!.toInt().toString() + "°",
                    R.drawable.min_temp) // Displays minimum temperature
                WeatherColumn(label = "Max Temp", value = weatherResponse.main.temp_max.toInt().toString() + "°",
                    R.drawable.max_temp) // Displays maximum temperature
                WeatherColumn(label = "Feels Like", value = weatherResponse.main.temp.toInt().toString() + "°",
                    R.drawable.feels_like) // Displays "feels like" temperature
            }

            Spacer(modifier = Modifier.height(16.dp)) // Adds vertical space

            // Second row: Pressure, Humidity, Wind speed
            Row(
                modifier = Modifier.fillMaxWidth(), // Fills the entire width
                horizontalArrangement = Arrangement.SpaceEvenly // Spaces elements evenly in the row
            ) {
                WeatherColumn(label = "Pressure", value = weatherResponse.main?.pressure.toString() + "Pa",
                    R.drawable.pressure) // Displays pressure in Pascals
                WeatherColumn(label = "Humidity", value = weatherResponse.main?.humidity.toString() + "g/m3",
                    R.drawable.humidity) // Displays humidity
                WeatherColumn(label = "Wind", value = weatherResponse.wind?.speed!!.toInt().toString(),
                    R.drawable.wind) // Displays wind speed
            }
        }
    }
}


@Composable
fun WeatherColumn(label: String, value: String, drawableId: Int) {
    // This function creates a card displaying weather details (label, icon, and value)
    Card(
        modifier = Modifier
            .width(100.dp) // Card width
            .height(120.dp), // Card height
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(14.dp)), // Sets the corner size to 14.dp
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Card elevation for shadow effect
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray // Card background color
        )
    ) {
        // Column to arrange the content inside the card vertically
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Center content horizontally
            verticalArrangement = Arrangement.Center, // Center content vertically
            modifier = Modifier
                .fillMaxSize() // Fill the card size
                .padding(10.dp) // Padding around the column content
        ) {
            // Display the weather label (e.g., "Min Temp", "Humidity")
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 14.sp, // Text size
                    color = Color.White, // Text color
                    fontFamily = Utils.poppingMedium // Custom font style
                )
            )
            Spacer(modifier = Modifier.height(8.dp)) // Adds space between the label and the image
            // Displays the weather icon
            Image(
                painter = painterResource(id = drawableId), // Image resource for the icon
                contentDescription = label, // Description for accessibility
                modifier = Modifier.size(35.dp) // Icon size
            )
            Spacer(modifier = Modifier.height(8.dp)) // Adds space between the image and the value
            // Displays the weather value (e.g., temperature, humidity percentage)
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 16.sp, // Text size
                    color = Color.White, // Text color
                    fontFamily = Utils.poppingBold // Custom font style
                )
            )
        }
    }
}

@Composable
fun GreetingBasedOnTime(weatherResponse: WeatherResponse) {
    // This function greets the user based on the time of day (morning, afternoon, evening)

    val calendar = Calendar.getInstance() // Get the current time
    val hour = calendar.get(Calendar.HOUR_OF_DAY) // Extract the hour in 24-hour format

    // Determine the greeting based on the current hour
    val greeting = when (hour) {
        in 0..11 -> "Good Morning" // 0-11 hours: Morning greeting
        in 12..17 -> "Good Afternoon" // 12-17 hours: Afternoon greeting
        else -> "Good Evening" // After 17 hours: Evening greeting
    }

    // Column to display the city name and greeting message
    Column(
        horizontalAlignment = Alignment.Start, // Align content to the start (left)
        modifier = Modifier.fillMaxWidth() // Fill the full width of the container
    ) {
        // Displays the city name from the weather response
        Text(
            text = weatherResponse.name!!, // City name
            style = TextStyle(
                fontSize = 25.sp, // Text size
                color = Color.White, // Text color
                fontFamily = Utils.poppingMedium // Custom font style
            )
        )
        // Displays the greeting based on time
        Text(
            text = greeting, // Greeting message
            style = TextStyle(
                fontSize = 30.sp, // Bigger text size
                color = Color.White, // Text color
                fontFamily = Utils.poppingMedium // Custom font style
            )
        )
    }
}

@SuppressLint("SimpleDateFormat")
private fun formatTimestamp(timestamp: Long): String {
    // This function formats the given timestamp into a readable date and time string
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) // Date format: "dd MMM yyyy, hh:mm a"
    return sdf.format(Date(timestamp * 1000)) // Convert timestamp (in seconds) to milliseconds and format
}
