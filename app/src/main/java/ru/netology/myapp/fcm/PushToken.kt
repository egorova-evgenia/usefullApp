package ru.netology.myapp.fcm

data class PushToken(val token:String)
//FromGson
// data???
data class Msg(
    val content: String,
    val recipientId: Long?
)
