package ru.netology.myapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.myapp.dto.Post
import ru.netology.myapp.repository.PostRepository
import ru.netology.myapp.repository.PostRepositoryInMemory
val newPostId=-1
val empty = Post(
    newPostId,
    "Mary",
    "@tools:sample/avatars",
    "30 июля в 15.30",
    "",
    "",
    0,
    0,
    0,
    false
)

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemory()
    val data=repository.getAll()
    fun likeById(id: Int)=repository.likeById(id)
    fun shareById(id: Int)=repository.shareById(id)
    fun removeById(id: Int) = repository.removeById(id)

    val edited = MutableLiveData(empty)

    fun editContent(content: String) {
        edited.value?.let {
        val trimmed = content.trim()
        if (trimmed==it.content){
            return}
        edited.value=it.copy(content=trimmed)
        }
    }
    fun save(){
        edited.value?.let {
            repository.save(it)
            edited.value = empty

        }
    }
    fun edit(post: Post){
        edited.value =post
    }

    fun  cancelEdit(){
        edited.value =empty // сброс текста
    }

}