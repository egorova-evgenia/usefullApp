package ru.netology.myapp.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import ru.netology.myapp.dto.Post
import java.util.concurrent.TimeUnit

class PostRepositoryImp:PostRepository {

    private val client=OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson= Gson()
    private val typeToken = object : TypeToken<List<Post>>(){}

    companion object {
        private const val BASE_URL ="Http://10.0.2.2:9999" // это и есть сервер
        private val jsonType = "application/json".toMediaType()
    }


    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?:throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            }
    }

    override fun likeById(id: Long) {
        val request: Request = Request.Builder()
            .post(EMPTY_REQUEST)
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()
        client.newCall(request)
            .execute()
            .close()
    }
    override fun unLikeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()
        client.newCall(request)
            .execute()
            .close()
    }

//    override fun shareById(id: Long) {
//    }
    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id")
            .build()
        client.newCall(request)
            .execute()
            .close()
    }

    override fun save(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts")
            .build()
        client.newCall(request)
            .execute()
            .close()    }

    override fun getById(id: Long): Post {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/$id") // найти пост
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?:throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, Post:: class.java)
            }
    }

}