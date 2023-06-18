package ru.netology.myapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.myapp.db.AppDb
import ru.netology.myapp.dto.Media
import ru.netology.myapp.repository.PostRepository
import ru.netology.myapp.repository.PostRepositoryImp

class RegViewModel (application: Application): AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositoryImp(AppDb.getInstance(context = application).postDao())

    private val _avatarState = MutableLiveData<PhotoModel?>()
    val photoState: LiveData<PhotoModel?>
        get()=_avatarState
    fun changePhoto(photoModel: PhotoModel?) {
        _avatarState.value=photoModel
    }
//
    fun registerUser(login: String, password: String, name: String){
        viewModelScope.launch {
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
