package ru.netology.myapp.ServerService

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import ru.netology.myapp.BuildConfig
import ru.netology.myapp.auth.AuthState
import ru.netology.myapp.dto.Media
import ru.netology.myapp.dto.Post
import ru.netology.myapp.service.PushToken

private const val BASE_URL = "${BuildConfig.BASE_URL}api/"

interface ApiService {
    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("posts/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<Post>>

    @GET("posts/{id}/before")
    suspend fun getBefore(@Path("id") id: Long, @Query("count") count: Int): Response<List<Post>>

    @GET("posts/{id}/after")
    suspend fun getAfter(@Path("id") id: Long, @Query("count") count: Int): Response<List<Post>>


    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getById(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Long): Response<Post>

    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    @Multipart
    @POST("media")
    suspend fun uploadPhoto(@Part media: MultipartBody.Part): Response<Media>

    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun updateUser(@Field("login")   login: String, @Field("pass") pass: String): Response<AuthState>

    @FormUrlEncoded
    @POST("users/registration")
    suspend fun registerUser(@Field("login") login: String, @Field("pass") pass: String, @Field("name") name: String): Response<AuthState>

    @Multipart
    @POST("users/registration")
    suspend fun registerWithPhoto(
        @Part("login") login: RequestBody,
        @Part("pass") pass: RequestBody,
        @Part("name") name: RequestBody,
        @Part media: MultipartBody.Part,
    ): Response<AuthState>

    @POST("users/push-tokens")
    suspend fun saveToken(@Body token: PushToken) : Response<Unit>


    // 14_token
    interface ApiService {
        @POST("users/push-tokens")
        suspend fun sendPushToken(@Body token: PushToken): Response<Unit>
    }
}

//object Api {
//    private val retrofit = Retrofit.Builder()
//        .addConverterFactory(GsonConverterFactory.create())
//        .baseUrl(BASE_URL)
//        .client(okhttp)
//        .build()
//    val service: ApiService by lazy{
//        retrofit.create()
//    }
//}
//private val logging = HttpLoggingInterceptor().apply {
//    if (BuildConfig.DEBUG) {
//        level = HttpLoggingInterceptor.Level.BODY
//    }
//}
//
//private val okhttp = OkHttpClient.Builder()
//    .addInterceptor(logging)
//    .addInterceptor { chain ->
//        appAuth.authStateFlow.value.token?.let { token ->
//            chain
//                .request()
//                .newBuilder()
//                .addHeader("Authorization", token)
//                .build()
//                .apply { return@addInterceptor chain.proceed(this) }
//        }
//        return@addInterceptor chain.proceed(chain.request())
//        }.build()





