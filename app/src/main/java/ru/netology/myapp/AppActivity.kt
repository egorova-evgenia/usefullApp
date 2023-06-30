package ru.netology.myapp

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.myapp.auth.AppAuth
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity: AppCompatActivity(R.layout.activity_app) {

    @Inject
    lateinit var appAuth: AppAuth
}