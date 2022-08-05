package ru.netology.myapp.dto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class Post(
        val id: Long,
        val autor: String,
        val autorAvatar: String,
        val published: String,
        val content: String,
        val url: String ,
        var likes: Int,
        var shares: Int ,
        var viewes: Int ,
        var iLiked: Boolean = false
)

