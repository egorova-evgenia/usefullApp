package ru.netology.myapp.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.netology.myapp.repository.PostRepository
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
