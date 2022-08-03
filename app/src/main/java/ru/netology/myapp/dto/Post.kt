package ru.netology.myapp.dto

data class Post(
        val id: Long,
        val autor: String,
        val autorAvatar: String,
        val published: String,
        val content: String,
        val url: String ,
        var likes: Int,
        var share: Int ,
        var viewed: Int ,
        var iLiked: Boolean = false
)


