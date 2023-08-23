package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.myapp.ServerService.ApiService
import ru.netology.myapp.dto.Post
import ru.netology.myapp.appError.ApiError
import ru.netology.myapp.appError.NetworkError
import ru.netology.myapp.appError.UnknownError
import ru.netology.myapp.auth.AppAuth
import ru.netology.myapp.dao.PostDao
import ru.netology.myapp.dao.PostRemoteKeyDao
import ru.netology.myapp.db.AppDb
import ru.netology.myapp.dto.Ad
import ru.netology.myapp.dto.Attachment
import ru.netology.myapp.dto.AttachmentType
import ru.netology.myapp.dto.FeedItem
import ru.netology.myapp.dto.Media
import ru.netology.myapp.entity.PostEntity
import ru.netology.myapp.viewmodel.PhotoModel
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

class PostRepositoryImp @Inject constructor(
    private val appAuth: AppAuth,
    private val postDao: PostDao,
    private val apiService: ApiService,
    private val appDb: AppDb,
    private val postRemoteKeyDao: PostRemoteKeyDao,
) : PostRepository {

    @OptIn(ExperimentalPagingApi::class)
    override val dataToShow: Flow<PagingData<FeedItem>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = { postDao.getPagingSource() },
        remoteMediator = PostRemoteMediator(
            service = apiService,
            postDao = postDao,
            postRemoteKeyDao = postRemoteKeyDao,
            appDb = appDb
        )
    ).flow
        .map {
            it.map(PostEntity::toDto)
                .insertSeparators { previous, _ ->
                    if (previous?.id?.rem(5) == 0L) {
                        Ad(Random.nextLong(), "figma.jpg")
                    } else {
                        null
                    }
                }
        }
    override suspend fun likeById(id: Long) {
        postDao.likeById(id)
        try {
            val response = apiService.likeById(id)
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
            val response = apiService.dislikeById(id)
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
            val response = apiService.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override fun getPostById(id: Long): LiveData<Post?> =
        postDao.getById(id).map {
            it!!.toDto()
        }.asLiveData()

    override suspend fun save(post: Post) {
        postDao.insert(PostEntity.fromDto(post))
        try {
            val response = apiService.save(post)
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
            val response =  apiService.save(
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
                    "file", photo.file.name, it.asRequestBody()
                )
            }
            val response = media?.let { apiService.uploadPhoto(it) }
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

    override suspend fun updateUser(login: String, password: String) {
        try {
            val response =apiService.updateUser(login,password)
            // получили AuthState         var id:Long =0Lvar token:String? = null
                if (!response.isSuccessful){
                    throw ApiError(response.code(), response.message())
                }
            val body =response.body() ?: throw ApiError(response.code(), response.message())
            println("id:  "+ body.id )
            println("token:  "+body.token)

            body.token?.let { appAuth.setAuth(body.id, it) }
//
        } catch (e: IOException) {
            println("e1")
            throw NetworkError
        } catch (e: Exception) {
            println("e2")
            throw UnknownError
        }
    }

    override suspend fun registerUser(login: String, password: String, name: String) {
        try {
            val response =apiService.registerUser(login,password,name)
            if (!response.isSuccessful){
                throw ApiError(response.code(), response.message())
            }
            val body =response.body() ?: throw ApiError(response.code(), response.message())
            println("id:  "+ body.id )
            println("token:  "+body.token)

            body.token?.let { appAuth.setAuth(body.id, it) }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun registerWithPhoto(
        login: String,
        password: String,
        name: String,
        photo: PhotoModel
    ) {
        val media = photo.file?.let {
            MultipartBody.Part.createFormData(
                "file", photo.file.name, it.asRequestBody()
            )
        }
        try {
            val response = media?.let {
                apiService.registerWithPhoto(
                    login.toRequestBody("text/plain".toMediaType()),
                    password.toRequestBody("text/plain".toMediaType()),
                    name.toRequestBody("text/plain".toMediaType()),
                    it
                )
            }
            if (!response?.isSuccessful!!){
                throw ApiError(response.code(), response.message())
            }
            val body =response.body() ?: throw ApiError(response.code(), response.message())
            println("id:  "+ body.id )
            println("token:  "+body.token)

            body.token?.let { appAuth.setAuth(body.id, it) }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}






