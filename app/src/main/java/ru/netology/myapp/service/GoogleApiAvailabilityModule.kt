package ru.netology.myapp.service

import android.content.Context
import androidx.room.Room
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.myapp.dao.PostDao
import ru.netology.myapp.db.AppDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class GoogleApiAvailabilityModule {
    @Singleton
    @Provides
    fun provideGoogleApiAvailability(): GoogleApiAvailability
    = GoogleApiAvailability.getInstance()
}

@InstallIn(SingletonComponent::class)
@Module
class FirebaseModule {
    @Singleton
    @Provides
    fun provideFireBaseMessaging():
            FirebaseMessaging = FirebaseMessaging.getInstance()
}

