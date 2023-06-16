package ru.netology.myapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.myapp.db.AppDb
import ru.netology.myapp.repository.PostRepository
import ru.netology.myapp.repository.PostRepositoryImp

class SignInViewModel(application: Application): AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositoryImp(AppDb.getInstance(context = application).postDao())

    fun updateUser(login: String, pass: String){
        viewModelScope.launch {
            try {
                repository.updateUser(login,pass)
            } catch (e: Exception){
                println(e)
//                тост может быть
//                _dataState.value = FeedModelState(error = true)
            }

        }
    }
}

//fun loadPosts() = scope.launch {// корутина
//    try {
//        _dataState.value = FeedModelState(loading = true)
//        repository.getAll()
//        _dataState.value = FeedModelState()// обновляем MutableLiveData
//    } catch (e: Exception) {
//        _dataState.value = FeedModelState(error = true)
//    }
//}