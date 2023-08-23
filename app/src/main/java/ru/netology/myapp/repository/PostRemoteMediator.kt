package ru.netology.myapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.netology.myapp.ServerService.ApiService
import ru.netology.myapp.appError.ApiError
import ru.netology.myapp.dao.PostDao
import ru.netology.myapp.dao.PostRemoteKeyDao
import ru.netology.myapp.db.AppDb
import ru.netology.myapp.dto.Post
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
                LoadType.REFRESH -> service.getLatest(state.config.pageSize)
                LoadType.PREPEND -> {
                    val id = postRemoteKeyDao.max() ?: return MediatorResult.Success(false)
                    service.getAfter(id, state.config.pageSize)
                }

                LoadType.APPEND -> {
                    val id = postRemoteKeyDao.min() ?: return MediatorResult.Success(false)
                    service.getAfter(id, state.config.pageSize)
                }
            }
            if (!result.isSuccessful) {
                throw ApiError(result.code(), result.message())
            }

            val body = result.body() ?: throw ApiError(result.code(), result.message())
//            запись в базу данных

            appDb.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
                        postDao.removeAll()
                        postRemoteKeyDao.insert(
                            listOf(
                                PostRemoteKeyEntity(
                                    PostRemoteKeyEntity.KeyType.AFTER,
                                    body.first().id,
                                ),
                                PostRemoteKeyEntity(
                                    PostRemoteKeyEntity.KeyType.BEFORE,
                                    body.last().id,
                                )
                            )
                        )
                    }

                    LoadType.PREPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                PostRemoteKeyEntity.KeyType.AFTER,
                                body.first().id,
                            )
                        )
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