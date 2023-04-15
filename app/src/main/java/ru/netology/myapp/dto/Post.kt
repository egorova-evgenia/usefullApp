package ru.netology.myapp.dto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class Post(
        val id: Long,
        val author: String,
        val content: String,
        val published: Long,
        val likedByMe: Boolean,
        val likes: Int = 0,
)

