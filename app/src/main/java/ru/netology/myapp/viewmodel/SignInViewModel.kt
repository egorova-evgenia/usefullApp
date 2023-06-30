package ru.netology.myapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.netology.myapp.auth.AppAuth
import ru.netology.myapp.db.AppDb
import ru.netology.myapp.repository.PostRepository
import ru.netology.myapp.repository.PostRepositoryImp
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: PostRepository,
) : ViewModel(){
    private val scope = MainScope()
    fun updateUser(login: String, pass: String){
        scope.launch {
            try {
                repository.updateUser(login,pass)
            } catch (e: Exception){
                println(e)
            }
        }
    }
}
