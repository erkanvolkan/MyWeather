package com.my.weatther.screens


import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.my.weatther.LocationHelper
import com.my.weatther.R
import com.my.weatther.Utils
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.weather),
            contentDescription = "Splash Image",
            modifier = Modifier.size(250.dp)
        )
    }

    LaunchedEffect(Unit) {
        delay(5000) // Delay before rechecking for city name
        navController.navigate("Home") {
            popUpTo("Splash") { inclusive = true }
        }
    }
}
