package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.myapp.ServerService.EventApiService
import ru.netology.myapp.appError.ApiError
import ru.netology.myapp.appError.NetworkError
import ru.netology.myapp.appError.UnknownError
import ru.netology.myapp.dao.EventDao
import ru.netology.myapp.dao.EventRemoteKeyDao
import ru.netology.myapp.db.AppDb
import ru.netology.myapp.dto.Attachment
import ru.netology.myapp.dto.AttachmentType
import ru.netology.myapp.dto.Event
import ru.netology.myapp.dto.Media
import ru.netology.myapp.entity.EventEntity
import ru.netology.myapp.viewmodel.AttachmentForSaving
import java.io.IOException
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
//    private val appAuth: AppAuth,
    private val eventDao: EventDao,
    private val apiEventService: EventApiService,
    private val appDb: AppDb,
    private val eventRemoteKeyDao: EventRemoteKeyDao,
) : EventRepository {

    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<Event>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = { eventDao.getPagingSource() },
        remoteMediator = EventRemoteMediator(
            service = apiEventService,
            eventDao = eventDao,
            eventRemoteKeyDao = eventRemoteKeyDao,
            appDb = appDb
        )
    ).flow
        .map {
            it.map(EventEntity::toEventDto)
        }

    override suspend fun likeById(id: Int) {
        eventDao.likeById(id)
        try {
            val response = apiEventService.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            eventDao.insert(EventEntity.fromDto(body))//один

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun disLikeById(id: Int) {
        eventDao.likeById(id)
        try {
            val response = apiEventService.dislikeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            eventDao.insert(EventEntity.fromDto(body))//один

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }

    }

    override suspend fun removeById(id: Int) {
        eventDao.removeById(id)
        try {
            val response = apiEventService.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override fun getItemById(id: Int): LiveData<Event?> =
        eventDao.getById(id).map {
            it!!.toEventDto()
        }.asLiveData()

    override suspend fun save(event: Event) {
        eventDao.insert(EventEntity.fromDto(event))
        try {
            val response = apiEventService.save(event)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            eventDao.insert(EventEntity.fromDto(body).copy(isRemoteSaved = true))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveWithAttachment(
        event: Event,
        attachItem: AttachmentForSaving,
        attachType: AttachmentType?
    ) {
        try {
            val media = upload(attachItem)
            val response = apiEventService.save(
                event.copy(
//                    todo что делать с этими !!
                    attachment = Attachment(media.id, attachType!!)
                )
            )
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            eventDao.insert(EventEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    private suspend fun upload(attachItem: AttachmentForSaving): Media {
        try {
            val media = attachItem.file?.let {
                MultipartBody.Part.createFormData(
                    "file", attachItem.file.name, it.asRequestBody()
                )
            }
            val response = media?.let { apiEventService.uploadPhoto(it) }
            if (!response?.isSuccessful!!) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

}