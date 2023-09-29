package ru.netology.myapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.netology.myapp.ServerService.ApiService
import ru.netology.myapp.appError.ApiError
import ru.netology.myapp.dao.PostDao
import ru.netology.myapp.dao.PostRemoteKeyDao
import ru.netology.myapp.db.AppDb
import ru.netology.myapp.entity.PostEntity
import ru.netology.myapp.entity.PostRemoteKeyEntity
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val service: ApiService,
    private val postDao: PostDao,
    private val postRemoteKeyDao: PostRemoteKeyDao,
    private val appDb: AppDb,
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {
            val result = when (loadType) {
                LoadType.REFRESH -> {
                    println("here3 2" + postRemoteKeyDao.max())
                    if (postRemoteKeyDao.max() != null) {
                        service.getAfter(postRemoteKeyDao.max()!!, state.config.pageSize)
                    } else service.getLatest(state.config.pageSize)
                }

                LoadType.PREPEND -> {
//                    val id = postRemoteKeyDao.max() ?:
                    return MediatorResult.Success(false)
//                    service.getAfter(id, state.config.pageSize)
                }

                LoadType.APPEND -> {
                    val id = postRemoteKeyDao.min() ?: return MediatorResult.Success(false)
                    service.getBefore(id, state.config.pageSize)
                }
            }
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
                        if (postDao.isEmpty()) {
                            postRemoteKeyDao.insert(
                                listOf(
                                    PostRemoteKeyEntity(
                                        type = PostRemoteKeyEntity.KeyType.AFTER,
                                        key = body.first().id
                                    ),
                                    PostRemoteKeyEntity(
                                        type = PostRemoteKeyEntity.KeyType.BEFORE,
                                        key = body.last().id
                                    )
                                )
                            )
                        } else {
// ели пуст, то работает как рефреш, если с данными, как препенд
                            postRemoteKeyDao.insert(
                                listOf(
                                    PostRemoteKeyEntity(
                                        type = PostRemoteKeyEntity.KeyType.AFTER,
                                        key = body.first().id
                                    ),
                                )
                            )
                        }
                    }

                    LoadType.PREPEND -> {
//                        postRemoteKeyDao.insert(
//                            PostRemoteKeyEntity(
//                                PostRemoteKeyEntity.KeyType.AFTER,
//                                body.first().id,
//                            )
//                        )
                    }

                    LoadType.APPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                PostRemoteKeyEntity.KeyType.BEFORE,
                                body.last().id,
                            )
                        )
                    }
                }
                postDao.insert(body.map(PostEntity::fromDto))
            }
            return MediatorResult.Success(body.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        }
    }

}