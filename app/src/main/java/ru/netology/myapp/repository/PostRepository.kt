package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import ru.netology.myapp.dto.Post


interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Int)
    fun shareById(id: Int)
    fun removeById(id: Int)
    fun save(post: Post)
    fun findPost(id: Int): Post
}