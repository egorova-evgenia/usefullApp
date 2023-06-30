package ru.netology.myapp

import android.app.Application
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
@AndroidEntryPoint
class App: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}