package ru.netology.myapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.myapp.R
import ru.netology.myapp.auth.AppAuth
import ru.netology.myapp.service.Msg
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val msg: Msg = gson.fromJson(message.data[content], Msg::class.java)
        val recipientId = msg.recipientId
        val content=msg.content
        val myId = appAuth.authStateFlow.value.id
        when(recipientId) {
            null -> println ("массовая рассылка:  " + message.data["content"])
            myId -> println ("персональное сообщение:  " + content)
            else -> {appAuth.sendPushToken()}
        }
    }

    override fun onNewToken(token: String) {
        appAuth.sendPushToken(token)
    }
}
