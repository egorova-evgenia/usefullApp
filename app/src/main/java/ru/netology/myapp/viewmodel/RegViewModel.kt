package ru.netology.myapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.netology.myapp.auth.AppAuth
import ru.netology.myapp.repository.PostRepository
import javax.inject.Inject

@HiltViewModel
class RegViewModel @Inject constructor(
    private val repository: PostRepository,
) : ViewModel(){
    private val scope = MainScope()

    private val _avatarState = MutableLiveData<PhotoModel?>()
    val photoState: LiveData<PhotoModel?>
        get()=_avatarState
    fun changePhoto(photoModel: PhotoModel?) {
        _avatarState.value=photoModel
    }
//
    fun registerUser(login: String, password: String, name: String){
    scope.launch {
            try {
                when (_avatarState.value) {
                    null -> repository.registerUser(login,password,name)
                    else -> _avatarState.value?.file.let { file ->
                        repository.registerWithPhoto(login,password,name, _avatarState.value!!)
                    }
                }
            } catch (e: Exception){
                println(e)
            }
        }
    }

}
