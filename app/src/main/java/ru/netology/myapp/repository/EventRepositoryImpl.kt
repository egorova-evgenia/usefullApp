package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.netology.myapp.ServerService.ApiService
import ru.netology.myapp.ServerService.EventApiService
import ru.netology.myapp.dao.EventDao
import ru.netology.myapp.dao.EventRemoteKeyDao
import ru.netology.myapp.dao.PostRemoteKeyDao
import ru.netology.myapp.db.AppDb
import ru.netology.myapp.dto.Ad
import ru.netology.myapp.dto.AttachmentType
import ru.netology.myapp.dto.Event
import ru.netology.myapp.dto.FeedItem
import ru.netology.myapp.dto.Post
import ru.netology.myapp.entity.EventEntity
import ru.netology.myapp.entity.PostEntity
import ru.netology.myapp.viewmodel.AttachmentModel
import javax.inject.Inject
import kotlin.random.Random

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

}