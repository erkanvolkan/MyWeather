package com.my.weatther

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.my.weatther.models.WeatherResponse
import com.my.weatther.screens.SplashScreen
import com.my.weatther.screens.WeatherScreen
import com.my.weatther.ui.theme.MyWeatherTheme
import com.my.weatther.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var locationHelper: LocationHelper // Helper for managing location permissions and updates

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationHelper = LocationHelper(this) // Initialize LocationHelper
        locationHelper.checkLocationPermission() // Check for location permissions

        enableEdgeToEdge() // Enable edge-to-edge layout
        changeStatusBarColor(Color.DarkGray) // Set status bar color

        // Set content view with theme and navigation host
        setContent {
            MyWeatherTheme {
                AppNavHost(navHostController = rememberNavController()) // Set up navigation
            }
        }
    }

    // Enables edge-to-edge layout for the activity
    private fun enableEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    // Changes the status bar color and adjusts icon appearance
    private fun changeStatusBarColor(color: Color) {
        window.statusBarColor = color.toArgb() // Set status bar color
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false // Use light icons for dark status bar
    }

    // Handle location permission result (deprecated method)
    @Deprecated("This method has been deprecated in favor of using the Activity Result API...")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults) // Delegate to LocationHelper
    }
}

// Composable function for setting up navigation
@Composable
fun AppNavHost(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "Splash") { // Define navigation graph
        composable("Splash") {
            SplashScreen(navHostController) // Navigate to SplashScreen
        }
        composable("Home") {
            WeatherScreen() // Navigate to WeatherScreen
        }
    }
}
