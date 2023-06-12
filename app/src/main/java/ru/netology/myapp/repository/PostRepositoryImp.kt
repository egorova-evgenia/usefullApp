package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.myapp.ServerService.PostApi
import ru.netology.myapp.dto.Post
import ru.netology.myapp.appError.ApiError
import ru.netology.myapp.appError.NetworkError
import ru.netology.myapp.appError.UnknownError
import ru.netology.myapp.dao.PostDao
import ru.netology.myapp.entity.PostEntity
import ru.netology.myapp.entity.toDto
import ru.netology.myapp.entity.toEntity
import java.io.IOException
class PostRepositoryImp(private val postDao: PostDao):PostRepository {
    override val data: LiveData<List<Post>>
        get() =  postDao.getAll().map(List<PostEntity>::toDto)

    override suspend fun getAll() {
        val response = PostApi.service.getAll()
        if (!response.isSuccessful) throw ApiError(response.code(), response.message())
        val body = response.body() ?: throw ApiError(response.code(), response.message())
        postDao.insert(body.toEntity())

//        try {
//            val response = PostApi.service.getAll()
//            if (!response.isSuccessful) {
//                throw ApiError(response.code(), response.message())
//            }
//            val body = response.body() ?: throw ApiError(response.code(), response.message())
//            dao.insert(body.toEntity()) // получили норм ответ, вставляем в локал бд
//        } catch (e: IOException) {
//            throw NetworkError
//        } catch (e: Exception) {
//            throw UnknownError
//        }
    }
    override suspend fun likeById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun removeById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun save(post: Post) {
        TODO("Not yet implemented")
    }


}


