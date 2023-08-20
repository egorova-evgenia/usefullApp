package ru.netology.myapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.myapp.dto.Post
import ru.netology.myapp.viewmodel.PhotoModel
import java.security.AccessControlContext


interface PostRepository {
//    val data: Flow<List<Post>>

    val dataToShow: Flow<PagingData<Post>>

    //    fun getNewer(id: Long): Flow<Int>
//    suspend fun getAll()
    fun getPostById(id: Long): LiveData<Post?>
    suspend fun likeById(id: Long)
    suspend fun disLikeById(id: Long)
    suspend fun removeById(id: Long)
    suspend fun save(post: Post)
    suspend fun saveWithAttachment(post: Post, photo: PhotoModel)
    suspend fun changeHidden()
    suspend fun updateUser(login: String, password: String)
    suspend fun registerUser(login: String, password: String, name: String)
    suspend fun registerWithPhoto(login: String, password: String, name: String, photo: PhotoModel)

}