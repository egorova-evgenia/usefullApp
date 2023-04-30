package ru.netology.myapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import ru.netology.myapp.dto.Post
import ru.netology.myapp.eventsAndOther.SingleLiveEvent
import ru.netology.myapp.repository.PostRepository
import ru.netology.myapp.repository.PostRepositoryImp
import java.io.IOException
import kotlin.concurrent.thread

val newPostId=0L
val empty = Post(
    newPostId,
    "Mary",
    "  ",
    1,
    false,
    0
)

class PostViewModel(application: Application) : AndroidViewModel(application){

    private val repository: PostRepository = PostRepositoryImp()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.GetAllCallback {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }
            override fun onError(e: Exception){
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun findPost(id: Long): Post? {
        return _data.value?.posts?.find {
            it.id==id
        }

    }

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun likeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        repository.likeById(id, object : PostRepository.NothingCallback{
            override fun onSuccess(){
                val new=old.map {
                    if (it.id != id) it else it.copy(likedByMe = true, likes = it.likes + 1)
                }
                _data.postValue(FeedModel(posts=new))
            }
            override fun onError (e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun unLikeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        repository.unLikeById(id, object : PostRepository.NothingCallback{
            override fun onSuccess(){
                val new = old.map {
                    if (it.id != id) it else it.copy(likedByMe = false, likes = it.likes - 1)
                }
                _data.postValue(FeedModel(posts = new))
            }
            override fun onError (e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })

    }


    fun shareById(id: Long) {

    }
    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        repository.removeById(id, object : PostRepository.NothingCallback{
            override fun onSuccess(){
                _data.postValue(FeedModel(old.filter { it.id != id }))
            }
            override fun onError (e: Exception) {
                _data.postValue(_data.value?.copy(posts = old)) // если не получилось, возвращаем бекап
            }
        })
    }

    fun editContent(content: String) {
        edited.value?.let {
        val trimmed = content.trim()
        if (trimmed==it.content){
            return}
        edited.value=it.copy(content=trimmed)
        }
    }

    fun save() {
        edited.value?.let {
            repository.save(it, object : PostRepository.NothingCallback{
                override fun onSuccess() {
                    _postCreated.postValue(Unit) //  используем метод, потому что в thread
//
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
            edited.value = empty
        }
    }


    fun edit(post: Post){
        edited.value =post
    }

    fun  cancelEdit(){
        edited.value =empty // сброс текста
    }

    fun refresh(){
        loadPosts()
    }

//    fun findPost(id: Long?): Post {
//        thread {
//            try {
//                return@thread repository.getById(id)
//            } catch (e: IOException) {
//                // если не находится пост, ничего не делать
//            }
//         }
//    }

//    fun findPost(id: Long): Post = repository.getById(id)



}