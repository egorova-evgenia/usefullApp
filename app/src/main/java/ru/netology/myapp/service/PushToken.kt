package ru.netology.myapp.service

data class PushToken(val token:String)
//FromGson
// data???
data class Msg(
    val content: String,
    val recipientId: Long?
)
