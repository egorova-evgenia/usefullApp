package ru.netology.myapp.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.myapp.repository.PostRepository
import ru.netology.myapp.repository.PostRepositoryInMemory

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemory()
    val data=repository.get()
    fun getValue() {}
    fun like() {
        repository.like()
    }
    fun share() {
        repository.share()
    }
}