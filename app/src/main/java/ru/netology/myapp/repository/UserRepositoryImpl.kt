package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import ru.netology.myapp.ServerService.ApiService
import ru.netology.myapp.ServerService.UserApi
import ru.netology.myapp.appError.ApiError
import ru.netology.myapp.appError.NetworkError
import ru.netology.myapp.appError.UnknownError
import ru.netology.myapp.dto.Job
import ru.netology.myapp.dto.User
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: UserApi,
) : UserRepository {
//    override suspend fun getUserById(id: Int): LiveData<User>? {
//        try {
//            val userResponse = apiService.getUserById(id)
//            val body =
//                userResponse.body() ?: throw ApiError(userResponse.code(), userResponse.message())
//            return userResponse.body()
//        } catch (e: IOException) {
//            e.printStackTrace()
//            throw NetworkError
//        } catch (e: Exception) {
//            e.printStackTrace()
//            throw UnknownError
//        }
//    }


    override suspend fun getUserById(id: Int): User? {
        try {
            println("here7    ")
            val userResponse = apiService.getUserById(id)
            println("userResponse get")
            val body =
                userResponse.body() ?: throw ApiError(userResponse.code(), userResponse.message())
            println("bodyU   " + userResponse.body())
            return userResponse.body()
        } catch (e: IOException) {
            e.printStackTrace()
            throw NetworkError
        } catch (e: Exception) {
            e.printStackTrace()
            throw UnknownError
        }
    }


    override suspend fun getJobListForUser(id: Int): List<Job> {
        try {
            val response = apiService.getJobsForUser(id)
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            return body
        } catch (e: IOException) {
            e.printStackTrace()
            throw NetworkError
        } catch (e: Exception) {
            e.printStackTrace()
            throw UnknownError
        }
    }

    override suspend fun addJobForUser(id: Int): List<Job>? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteJobForUser(id: Int): List<Job>? {
        TODO("Not yet implemented")
    }


//    {
//        override val jobsData: Flow<List<JobModel>> = jobDao.getAll()
//            .map(List<JobEntity>::toModel)
//            .flowOn(Dispatchers.Default)

//        override suspend fun getJobsByUserId(id: Int): List<Job> {
//            try {
//                val response = jobsApi.getJobsById(id)
//                val body = response.body() ?: throw ApiError(response.code(), response.message())
//                val apiResult = body.map {
//                    it.copy(userId = id)
//                }.map { it.toEntity() }
//                jobDao.insert(apiResult)
//                val daoResult = jobDao.getByAuthorId(id).map { it.toModel() }
//                return daoResult
//
//            } catch (e: ApiError) {
//                throw ApiError(e.responseCode, e.message)
//            } catch (e: IOException) {
//                throw NetworkError("error_network")
//            }
//            catch (e: Exception) {
//                throw UnknownError("error_unknown")
//            }
//        }

//        override suspend fun updateJobsByUserId(id: Long): List<JobModel>? {
//            try {
//                val result = jobsApi.getJobsById(id)
//                result.body()?.let { it -> jobDao.insert(it.map { it.toEntity() }) }
//                return result.body()?.map { it.toModel() }
//            } catch (e: ApiError) {
//                throw ApiError(e.responseCode, e.message)
//            } catch (e: IOException) {
//                throw NetworkError("error_network")
//            }
//            catch (e: Exception) {
//                throw UnknownError("error_unknown")
//            }
//        }
//
//        override suspend fun create(job: JobCreateRequest) {
//            try {
//                jobsApi.createJob(job)
//            } catch (e: ApiError) {
//                throw ApiError(e.responseCode, e.message)
//            } catch (e: IOException) {
//                throw NetworkError("error_network")
//            }
//            catch (e: Exception) {
//                throw UnknownError("error_unknown")
//            }
//        }
//
//        override suspend fun removeById(id: Long) {
//            try {
//                jobsApi.removeById(id)
//                jobDao.removeById(id)
//            } catch (e: ApiError) {
//                throw ApiError(e.responseCode, e.message)
//            } catch (e: IOException) {
//                throw NetworkError("error_network")
//            }
//            catch (e: Exception) {
//                throw UnknownError("error_unknown")
//            }
//        }
//    }


}