package ru.netology.myapp.ServerService

import androidx.lifecycle.LiveData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import ru.netology.myapp.auth.AuthState
//import ru.netology.myapp.dto.ListPost
import ru.netology.myapp.dto.Media
import ru.netology.myapp.dto.Post
import ru.netology.myapp.dto.User

interface ApiService {
    // получение данных для paging
    @GET("posts/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<Post>>

    //    ListPost
    @GET("posts/{id}/before")
    suspend fun getBefore(@Path("id") id: Int, @Query("count") count: Int): Response<List<Post>>

    @GET("posts/{id}/after")
    suspend fun getAfter(@Path("id") id: Int, @Query("count") count: Int): Response<List<Post>>

//    todo на сервере есть, но в приложении, наверно, не нужны

    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Int): Response<List<Post>>

    // работа с постами
    @GET("posts/{id}")
    suspend fun getById(@Path("id") id: Int): Response<Post>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Int): Response<Unit>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Int): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Int): Response<Post>

    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    //    todo не только фото
    @Multipart
    @POST("media")
    suspend fun uploadPhoto(@Part media: MultipartBody.Part): Response<Media>

//  работа с юзерами, todo вынести в отдельный класс

//    вероятно, не нужно по той же самой причине

//    @GET("users")
//    suspend fun getAll(): Response<List<User>>

    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun updateUser(
        @Field("login") login: String,
        @Field("pass") pass: String
    ): Response<AuthState>

    @FormUrlEncoded
    @POST("users/registration")
    suspend fun registerUser(
        @Field("login") login: String,
        @Field("pass") pass: String,
        @Field("name") name: String
    ): Response<AuthState>

    @Multipart
    @POST("users/registration")
    suspend fun registerWithPhoto(
        @Part("login") login: RequestBody,
        @Part("pass") pass: RequestBody,
        @Part("name") name: RequestBody,
        @Part media: MultipartBody.Part,
    ): Response<AuthState>
//
//    @POST("users/push-tokens")
//    suspend fun saveToken(@Body token: PushToken): Response<Unit>
//
//    interface ApiService {
//        @POST("users/push-tokens")
//        suspend fun sendPushToken(@Body token: PushToken): Response<Unit>
//    }
//
}