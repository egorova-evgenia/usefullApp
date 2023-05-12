package ru.netology.myapp.repository

import android.content.Context
import ru.netology.myapp.dto.Post
import java.security.AccessControlContext


interface PostRepository {
//    fun getAll(): List<Post>
    fun likeById(id: Long, callback: NothingCallback)
    fun unLikeById(id: Long, callback: NothingCallback)
    fun removeById(id: Long, callback: NothingCallback)
    fun save(post: Post, callback: NothingCallback)
    fun getById(id: Long, callback: PostCallback)
    fun getAllAsync(callback: GetAllCallback, context: Context)
    interface GetAllCallback {
        fun onSuccess(posts: List<Post>){}
        fun onError(e: Exception){}
    }// клб возвращает список постов

    interface PostCallback {
        fun onSuccess(post: Post){}
        fun onError(e: Exception){}
    }// клб возвращает 1 пост

    interface NothingCallback {
        fun onSuccess(){}
        fun onError(e: Exception){}
    }// клб возвращает ничего
//    стр 31

}