package ru.netology.myapp.repository

import ru.netology.myapp.dto.Post


interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long)
    fun unLikeById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
    fun getById(id: Long): Post
}