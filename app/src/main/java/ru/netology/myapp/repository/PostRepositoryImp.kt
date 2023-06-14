package ru.netology.myapp.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.myapp.ServerService.PostApi
import ru.netology.myapp.dto.Post
import ru.netology.myapp.appError.ApiError
import ru.netology.myapp.appError.NetworkError
import ru.netology.myapp.appError.UnknownError
import ru.netology.myapp.dao.PostDao
import ru.netology.myapp.dto.Attachment
import ru.netology.myapp.dto.AttachmentType
import ru.netology.myapp.dto.Media
import ru.netology.myapp.entity.PostEntity
import ru.netology.myapp.entity.toDto
import ru.netology.myapp.entity.toEntity
import ru.netology.myapp.viewmodel.PhotoModel
import java.io.IOException
class PostRepositoryImp(private val postDao: PostDao):PostRepository {
    override val data = postDao.getAll()
        .map(List<PostEntity>:: toDto)
        .flowOn(Dispatchers.Default)
    //page 26
    override fun getNewer(id: Long): Flow<Int> =
        flow {
                while (true) {
                    try {
                        delay(10_000L)
                        val response = PostApi.service.getNewer(id)
                        val posts = response.body().orEmpty()
                        emit(posts.size)

                        postDao.insert(posts.toEntity().map { it.copy(toShow = false) })
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        val response = PostApi.service.getAll()
        if (!response.isSuccessful) throw ApiError(response.code(), response.message())
        val body = response.body() ?: throw ApiError(response.code(), response.message())
        val bodyEntity: List<PostEntity> = body.toEntity()
            for (it in bodyEntity){
                it.toShow = true
            }
        postDao.insert(bodyEntity)
    }
    override suspend fun likeById(id: Long) {
        postDao.likeById(id)
        try {
            val response = PostApi.service.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))//один

        }catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun disLikeById(id: Long) {
        postDao.likeById(id)
        try {
            val response = PostApi.service.dislikeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))//один

        }catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }

    }
    override suspend fun removeById(id: Long) {
        postDao.removeById(id)
        try {
            val response = PostApi.service.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }

    }

    override suspend fun save(post: Post) {
        postDao.insert(PostEntity.fromDto(post))
        try {
            val response = PostApi.service.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body).copy(isRemoteSaved = true))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveWithAttachment(post: Post, photo: PhotoModel) {
        try {
            val media = upload(photo)
            val response =  PostApi.service.save(
                post.copy(
                    attachment = Attachment(media.id, "фото",AttachmentType.IMAGE)
                )
            )
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    private suspend fun upload(photo: PhotoModel): Media {
        try {
            val media = photo.file?.let {
                MultipartBody.Part.createFormData(
                    "file", photo.file?.name, it.asRequestBody()
                )
            }
            val response = media?.let { PostApi.service.uploadPhoto(it) }
            if (!response?.isSuccessful!!){
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
    override suspend fun changeHidden() {
        postDao.toShowAll()
    }
}






