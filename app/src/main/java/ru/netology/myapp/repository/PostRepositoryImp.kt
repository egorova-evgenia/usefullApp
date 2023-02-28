package ru.netology.myapp.repository

import android.content.ContentProviderOperation.newCall
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.netology.myapp.dao.PostDao
import ru.netology.myapp.dto.Post
import ru.netology.myapp.entity.PostEntity
import ru.netology.myapp.exceptions.PostNotFoundException
import ru.netology.myapp.viewmodel.newPostId

class PostRepositoryImp(private val dao: PostDao
):PostRepository {

    private val client=OkHttpClient.Builder()
    private val gson= Gson()
    private val typeToken = object : TypeToken<List<Post>>(){}

    companion object {
        private const val BASE_URL ="Http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }


    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.toString() ?:throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            }
    }

    override fun likeById(id: Int) {
        dao.likeById(id)
//            data.value=posts
    }

    override fun shareById(id: Int) {
        dao.shareById(id)
//            data.value=posts
    }

    override fun removeById(id: Int) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
        val saved = dao.save(PostEntity.fromDto(post))
    }

    override fun findPost(id: Int): Post = dao.findPost(id).toDto()
}