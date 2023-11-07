package ru.netology.myapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.netology.myapp.auth.AppAuth
import ru.netology.myapp.dto.Job
import ru.netology.myapp.dto.Post
import ru.netology.myapp.dto.User
import ru.netology.myapp.eventsAndOther.SingleLiveEvent
import ru.netology.myapp.repository.UserRepositoryImpl
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepositoryImpl,
    appAuth: AppAuth,
) : ViewModel() {

    val myId = appAuth
        .authStateFlow.value.id

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    private var _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun getUserById(id: Int) {
        viewModelScope.launch {
            try {
                _user.postValue(userRepository.getUserById(id))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private var _myProfile = MutableLiveData<User>()
    val myProfile: LiveData<User>
        get() = _myProfile

    fun getMyProfile() {
        viewModelScope.launch {
            try {
                _myProfile.postValue(userRepository.getUserById(myId))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private val _userJobs = MutableLiveData<List<Job>>()
    val userJobs: LiveData<List<Job>>
        get() = _userJobs

    fun getJobsForUser(id: Int) {
        viewModelScope.launch {
            try {
                _userJobs.postValue(userRepository.getJobListForUser(id))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}