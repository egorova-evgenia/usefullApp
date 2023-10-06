package ru.netology.myapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.netology.myapp.dto.User
import ru.netology.myapp.repository.UserRepositoryImpl
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepositoryImpl,

    ) : ViewModel() {

    private val scope = MainScope()

    private var _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun getUserById(id: Int): LiveData<User> {
        scope.launch {
            try {
                _user = userRepository.getUserById(id) as MutableLiveData<User>
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return _user
    }
}