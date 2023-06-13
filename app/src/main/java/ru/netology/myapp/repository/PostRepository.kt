package ru.netology.myapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.netology.myapp.dto.Post
import java.security.AccessControlContext


interface PostRepository {
    val data: Flow<List<Post>>

    fun getNewer(id: Long): Flow<Int>
    suspend fun getAll()
    suspend fun likeById(id: Long)
    suspend fun disLikeById(id: Long)
    suspend fun removeById(id: Long)
    suspend fun save(post: Post)
    suspend fun changeHidden()

}