package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.myapp.dto.Attachment
import ru.netology.myapp.dto.AttachmentType
import ru.netology.myapp.dto.FeedItem
import ru.netology.myapp.dto.Post
import ru.netology.myapp.viewmodel.AttachmentModel


interface PostRepository {
    val dataToShow: Flow<PagingData<FeedItem>>

    suspend fun likeById(id: Int)
    suspend fun disLikeById(id: Int)
    suspend fun removeById(id: Int)
    fun getPostById(id: Int): LiveData<Post?>
    suspend fun save(post: Post)
    suspend fun saveWithAttachment(
        post: Post,
        attachItem: AttachmentModel,
        attachType: AttachmentType?
    )

    suspend fun changeHidden()
    suspend fun updateUser(login: String, password: String)
    suspend fun registerUser(login: String, password: String, name: String)
    suspend fun registerWithPhoto(
        login: String,
        password: String,
        name: String,
        photo: AttachmentModel
    )

}