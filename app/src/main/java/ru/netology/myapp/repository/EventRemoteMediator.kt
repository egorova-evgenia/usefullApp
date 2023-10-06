package ru.netology.myapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.netology.myapp.ServerService.ApiService
import ru.netology.myapp.ServerService.EventApiService
import ru.netology.myapp.appError.ApiError
import ru.netology.myapp.dao.EventDao
import ru.netology.myapp.dao.EventRemoteKeyDao
import ru.netology.myapp.dao.PostDao
import ru.netology.myapp.dao.PostRemoteKeyDao
import ru.netology.myapp.db.AppDb
import ru.netology.myapp.entity.EventEntity
import ru.netology.myapp.entity.EventRemoteKeyEntity
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class EventRemoteMediator(
    private val service: EventApiService,//todo
    private val eventDao: EventDao,
    private val eventRemoteKeyDao: EventRemoteKeyDao,
    private val appDb: AppDb,
) : RemoteMediator<Int, EventEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventEntity>
    ): MediatorResult {
        try {
            println("event1   " + loadType)
            val result = when (loadType) {

                LoadType.REFRESH -> {
                    if (eventRemoteKeyDao.max() != null) {
//
                        service.getAfter(eventRemoteKeyDao.max()!!, state.config.pageSize)
                    } else service.getLatest(state.config.pageSize)
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(false)
                }

                LoadType.APPEND -> {
                    val id = eventRemoteKeyDao.min() ?: return MediatorResult.Success(false)
                    service.getBefore(id, state.config.pageSize)
                }
            }
            println("event2   " + result.body())
            if (!result.isSuccessful) {
                println(result.message())
                throw ApiError(result.code(), result.message())
            }

            val body = result.body() ?: throw ApiError(result.code(), result.message())
//            запись в базу данных

            if (body.isEmpty()) return MediatorResult.Success(false)

            appDb.withTransaction {
                when (loadType) {

                    LoadType.REFRESH -> {
                        println("here4   " + result.body())
                        if (eventDao.isEmpty()) {
                            eventRemoteKeyDao.insert(
                                listOf(
                                    EventRemoteKeyEntity(
                                        type = EventRemoteKeyEntity.KeyType.AFTER,
                                        key = body.first().id
                                    ),
                                    EventRemoteKeyEntity(
                                        type = EventRemoteKeyEntity.KeyType.BEFORE,
                                        key = body.last().id
                                    )
                                )
                            )
                        } else {
// ели пуст, то работает как рефреш, если с данными, как препенд
                            eventRemoteKeyDao.insert(
                                listOf(
                                    EventRemoteKeyEntity(
                                        type = EventRemoteKeyEntity.KeyType.AFTER,
                                        key = body.first().id
                                    ),
                                )
                            )
                        }
                    }

                    LoadType.PREPEND -> {
                    }

                    LoadType.APPEND -> {
                        eventRemoteKeyDao.insert(
                            EventRemoteKeyEntity(
                                EventRemoteKeyEntity.KeyType.BEFORE,
                                body.last().id,
                            )
                        )
                    }
                }
                eventDao.insert(body.map(EventEntity::fromDto))
            }
            return MediatorResult.Success(body.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        }
    }
}
