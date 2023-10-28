package ru.netology.myapp.ServerService

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.myapp.dto.Event
import ru.netology.myapp.dto.Media
import ru.netology.myapp.dto.Post

interface EventApiService {
    @GET("events/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<Event>>

    @GET("events/{id}/before")
    suspend fun getBefore(@Path("id") id: Int, @Query("count") count: Int): Response<List<Event>>

    @GET("events/{id}/after")
    suspend fun getAfter(@Path("id") id: Int, @Query("count") count: Int): Response<List<Event>>

    @POST("events/{event_id}/likes")
    suspend fun likeById(@Path("id") id: Int): Response<Event>

    @DELETE("events/{event_id}/likes")
    suspend fun dislikeById(@Path("id") id: Int): Response<Event>

    @DELETE("events/{id}")
    suspend fun removeById(@Path("id") id: Int): Response<Unit>

    @POST("events")
    suspend fun save(@Body event: Event): Response<Event>

    @Multipart
    @POST("media")
    suspend fun uploadPhoto(@Part media: MultipartBody.Part): Response<Media>

}