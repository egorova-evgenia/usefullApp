package ru.netology.myapp.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.netology.myapp.ServerService.ApiService
import ru.netology.myapp.appError.ApiError
import ru.netology.myapp.dto.Post
import java.io.IOException

class PostPagingSource(private val service: ApiService) : PagingSource<Long, Post>() {

    override fun getRefreshKey(state: PagingState<Long, Post>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
        try {
            val result = when (params) {
                is LoadParams.Refresh -> service.getLatest(params.loadSize)
                is LoadParams.Prepend -> return LoadResult.Page(
                    data = emptyList(), nextKey = null, prevKey = params.key,
                )

                is LoadParams.Append -> service.getBefore(params.key, params.loadSize)
            }
            if (!result.isSuccessful) {
                println("here")
                throw HttpException(result)
            }

            val data = result.body().orEmpty()
            return LoadResult.Page(
                data = data, prevKey = params.key, nextKey = data.lastOrNull()?.id,
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }
}