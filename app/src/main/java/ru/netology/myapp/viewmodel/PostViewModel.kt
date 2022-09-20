package ru.netology.myapp.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.myapp.repository.PostRepository
import ru.netology.myapp.repository.PostRepositoryInMemory

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemory()
    val data=repository.getAll()
    fun likeById(id: Int)=repository.likeById(id)
    fun shareById(id: Int)=repository.shareById(id)
    fun removeById(id: Int) = repository.removeById(id)

}