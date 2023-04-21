package ru.netology.myapp.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import ru.netology.myapp.dto.Post
import java.io.IOException
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

    override fun getAllAsync(callback: PostRepository.GetAllCallback) {
        val request: Request =Request.Builder()
            .url("${BASE_URL}/api/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("Body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun likeById(id: Long, callback: PostRepository.NothingCallback) {
        val request: Request = Request.Builder()
            .post(EMPTY_REQUEST)
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()
        client.newCall(request)
            .enqueue(object : Callback{
                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess()
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
            }
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

            })
    }
    override fun unLikeById(id: Long, callback: PostRepository.NothingCallback) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()
        client.newCall(request)
            .enqueue(object : Callback{
                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess()
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

            })
    }

//    override fun shareById(id: Long) {
//    }
    override fun removeById(id: Long, callback: PostRepository.NothingCallback) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id")
            .build()
        client.newCall(request)
            .enqueue(object :Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess()
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun save(post: Post, callback: PostRepository.NothingCallback) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts")
            .build()
        client.newCall(request)
            .enqueue(object :Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess()
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun getById(id: Long, callback: PostRepository.PostCallback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/$id") // найти пост
            .build()
            client.newCall(request)
            .enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(gson.fromJson(response.body?.string(), Post::class.java))
                }
            })
    }

}