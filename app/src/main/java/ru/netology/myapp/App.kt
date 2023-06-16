package ru.netology.myapp

import android.app.Application
import ru.netology.myapp.auth.AppAuth

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        AppAuth.init(this)
    }
}