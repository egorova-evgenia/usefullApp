package ru.netology.myapp.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.myapp.repository.PostRepositoryInMemory

class PostViewModel: ViewModel() {
    private val repository = PostRepositoryInMemory()
    val data=repository.get()
    fun like() {
        repository.like()
    }
}