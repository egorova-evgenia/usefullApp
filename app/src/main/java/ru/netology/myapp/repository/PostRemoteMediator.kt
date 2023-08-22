package ru.netology.myapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
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
    private val appDb: AppDb
) : RemoteMediator<Int, PostEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {

//            определяем, какие данные подгружать
            val result = when (loadType) {
                LoadType.REFRESH -> {
                    if (postRemoteKeyDao.max() != null) {
                        service.getAfter(postRemoteKeyDao.max()!!, state.config.pageSize)
                    } else service.getLatest(state.config.pageSize)
                }
//                    service.getLatest(state.config.pageSize)
                LoadType.PREPEND -> {
//                    отмена загрузки
                    return MediatorResult.Success(false)
//                    val id= postRemoteKeyDao.max() ?: return MediatorResult.Success(false)
//                    service.getAfter(id, state.config.pageSize)
                }

                LoadType.APPEND -> {
                    val id = postRemoteKeyDao.min() ?: return MediatorResult.Success(false)
                    service.getAfter(id, state.config.pageSize)

//                    val remoteKeys = getRemoteKeyForLastItem(state)
//                    // If remoteKeys is null, that means the refresh result is not in the database yet.
//                    // We can return Success with endOfPaginationReached = false because Paging
//                    // will call this method again if RemoteKeys becomes non-null.
//                    // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
//                    // the end of pagination for append.
//                    val nextKey = remoteKeys?.nextKey
//                    if (nextKey == null) {
//                        return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//                    }
//                    nextKey

                }
            }
            if (!result.isSuccessful) {
                throw HttpException(result)
            }

            val body = result.body() ?: throw ApiError(
                result.code(),
                result.message(),
            )

            appDb.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
//                        postDao.clear() // затираем данные здесь
                        //ключи как-то получили выше
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
//                                body.first().id
//                            ),
//                        )
                    }

                    LoadType.APPEND -> {
//                        APPEND работал в обычном режиме.
//                        ничего не менять
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                PostRemoteKeyEntity.KeyType.BEFORE,
                                body.last().id
                            ),
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