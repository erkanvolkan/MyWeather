package com.my.weatther

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

object Utils {

    const val BASE_URL = "https://api.openweathermap.org/"
    //const val API_KEY = "437a36813cd559ebc4d472e96129de1f"
    const val API_KEY = ""

    val poppingBold = FontFamily(
        Font(R.font.poppings_bold)
    )

    val poppingMedium = FontFamily(
        Font(R.font.poppings_medium)
    )

    @JvmStatic
    fun saveCityNameToPreferences(context: Context,cityName: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("city_name", cityName)
        editor.apply()

    }

    @JvmStatic
    fun loadSavedCityName(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedCityName = sharedPreferences.getString("city_name", "")
        return savedCityName
    }



}
