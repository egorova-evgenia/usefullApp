package ru.netology.myapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.netology.myapp.appError.ApiError
import ru.netology.myapp.appError.NetworkError
import ru.netology.myapp.eventsAndOther.SingleLiveEvent
import ru.netology.myapp.repository.PostRepository
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: PostRepository,
) : ViewModel(){
    private val scope = MainScope()
    fun updateUser(login: String, pass: String) {
        scope.launch {
            try {
                repository.updateUser(login, pass)
                _resCreated.postValue(Unit)
            } catch (e: Exception) {
                when (e) {
                    is NetworkError -> {
                        _errorMessage.value = "error_network"
                    }

                    is ApiError -> {
                        _errorMessage.value = "error_api"
                    }

                    is UnknownError -> {
                        _errorMessage.value = "error_unknown"
                    }
                }

            }

        }
    }

    private val _resCreated = SingleLiveEvent<Unit>()
    val resCreated: LiveData<Unit>
        get() = _resCreated

    private val _errorMessage = SingleLiveEvent<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage


}
