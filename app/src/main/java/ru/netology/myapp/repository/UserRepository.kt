package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import ru.netology.myapp.dto.Job
import ru.netology.myapp.dto.User

interface UserRepository {
    suspend fun getUserById(id: Int): User?

    suspend fun getJobListForUser(id: Int): List<Job>?
    suspend fun addJobForUser(id: Int): List<Job>?
    suspend fun deleteJobForUser(id: Int): List<Job>?

}