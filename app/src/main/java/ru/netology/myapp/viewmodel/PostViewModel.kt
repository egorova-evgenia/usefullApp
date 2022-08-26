package ru.netology.myapp.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.myapp.repository.PostRepository
import ru.netology.myapp.repository.PostRepositoryInMemory

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemory()
    val data=repository.get()
//    fun like() {
//        repository.like()
//    }
//    fun share() {
//        repository.share()
//    }
    fun likeById(id: Int) {
        repository.likeById(id)
    }
}