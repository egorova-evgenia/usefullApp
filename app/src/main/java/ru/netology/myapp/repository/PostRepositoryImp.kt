package ru.netology.myapp.repository

import android.content.Context
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import ru.netology.myapp.ServerService.PostApi
import ru.netology.myapp.dto.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.AccessControlContext

class PostRepositoryImp:PostRepository {

//    private val client=OkHttpClient.Builder()
//        .connectTimeout(30, TimeUnit.SECONDS)
//        .build()
//    private val gson= Gson()
//    private val typeToken = object : TypeToken<List<Post>>(){}

    companion object {
        private const val BASE_URL ="Http://10.0.2.2:9999" // это и есть сервер
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAllAsync(callback: PostRepository.GetAllCallback, context: Context) {
        PostApi.service.getAll().enqueue(object : Callback<List<Post>>{
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
//                when (response.code()) {
//                    in 100..199 -> {
//                        Snackbar.make(view, response.message(), Snackbar.LENGTH_LONG).show()
//                        in 200..299 -> {
//                            Snackbar.make(view, response.message(), Snackbar.LENGTH_LONG)
//
//                    }
//                }

                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))

//                    Toast.makeText(context, response.message(),Toast.LENGTH_LONG ).show()
//                    Snackbar.make(binding.root, response.message(), Snackbar.LENGTH_LONG)
//                        .setAction(android.R.string.ok){
//                            finish()}.show()
                            return
                }
                callback.onSuccess(response.body()?: emptyList())
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }

    override fun likeById(id: Long, callback: PostRepository.NothingCallback) {
        PostApi.service.likeById(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {

                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess()
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }

            })
    }

    override fun unLikeById(id: Long, callback: PostRepository.NothingCallback) {
        PostApi.service.dislikeById(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess()
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }

            })
    }

//    override fun shareById(id: Long) {
//    }
    override fun removeById(id: Long, callback: PostRepository.NothingCallback) {
        PostApi.service.removeById(id)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess()
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }

    override fun save(post: Post, callback: PostRepository.NothingCallback) {
        PostApi.service.save(post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess()                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }

    override fun getById(id: Long, callback: PostRepository.PostCallback) {
        PostApi.service.getById(id)
            .enqueue(object : Callback<Post>{
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(response.body()?: throw RuntimeException("body is null"))
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }

}


