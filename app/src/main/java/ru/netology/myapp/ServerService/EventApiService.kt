package ru.netology.myapp.ServerService

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.myapp.dto.Event

interface EventApiService {
    @GET("events/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<Event>>

    @GET("events/{id}/before")
    suspend fun getBefore(@Path("id") id: Int, @Query("count") count: Int): Response<List<Event>>

    @GET("events/{id}/after")
    suspend fun getAfter(@Path("id") id: Int, @Query("count") count: Int): Response<List<Event>>

}