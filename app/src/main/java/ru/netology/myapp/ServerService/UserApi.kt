package ru.netology.myapp.ServerService

import androidx.lifecycle.LiveData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.myapp.dto.Job
import ru.netology.myapp.dto.User

interface UserApi {
    @GET("users/{user_id}")
    suspend fun getUserById(@Path("user_id") id: Int): Response<User>

    @GET("{user_id}/jobs/")
    suspend fun getJobsForUser(@Path("user_id") id: Int): Response<List<Job>>

    @POST("my/jobs/")
    suspend fun addNewJob(@Body job: Job): Response<Job>

    @DELETE("my/jobs/{job_id}/")
    suspend fun removeJobById(@Path("id") id: Int)
}