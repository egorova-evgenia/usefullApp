package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import ru.netology.myapp.ServerService.ApiService
import ru.netology.myapp.appError.ApiError
import ru.netology.myapp.appError.NetworkError
import ru.netology.myapp.appError.UnknownError
import ru.netology.myapp.dto.User
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getUserById(id: Int): LiveData<User>? {
        try {
            val userResponse = apiService.getUserById(id)
            val body =
                userResponse.body() ?: throw ApiError(userResponse.code(), userResponse.message())
            return userResponse.body()
        } catch (e: IOException) {
            e.printStackTrace()
            throw NetworkError
        } catch (e: Exception) {
            e.printStackTrace()
            throw UnknownError
        }
    }
}