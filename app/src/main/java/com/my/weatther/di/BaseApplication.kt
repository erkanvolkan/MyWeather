package com.my.weatther.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

    // Initializes Hilt dependency injection
    override fun onCreate() {
        super.onCreate()
    }
}
