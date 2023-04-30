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
    133,
    false,
    0
)
//val emptyPost = Post(
//    newPostId,
//    "Mary",
//    "  ",
//    133,
//    false,
//    0
//)
class PostViewModel(application: Application) : AndroidViewModel(application){

    private val repository: PostRepository = PostRepositoryImp()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    fun loadPosts() {
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val posts = repository.getAll()
                println(posts)
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                FeedModel(error = true)
            }.also(_data::postValue)
        }
    }



//    fun findPost(id: Long): Post {
//        thread {
//            _data.postValue(FeedModel(loading = true))
//            try {
//                val post = repository.getById(id)
//                FeedModel(post = post, empty = post.isEmpty())
//            } catch (e: IOException) {
//                FeedModel(error = true)
//            }.also {  }
//        }
//    }

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun likeById(id: Long) {
        thread {

            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .map { if (it.id == id) it.copy(likedByMe = true, likes = it.likes+1) else it }
                )
            )
            try {
                repository.likeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    fun unLikeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .map { if (it.id == id) it.copy(likedByMe = false, likes =it.likes-1) else it }
                )
            )
            try {
                repository.unLikeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }

        }
    }


    fun shareById(id: Long) {

    }
    fun removeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty() // как-бы бекап старых данных, чтобы в локальной  и удаленной бд были одни и теже данные
            _data.postValue(  // postValue потому что доступ внутри потока, иначе можно получить некорректный результат
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }  // удаляем
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old)) // если не получилось, возвращаем бекап
            }
        }
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
            thread {
                repository.save(it)
                _postCreated.postValue(Unit) //  используем метод, потому что в thread
            }
        }
        edited.value = empty
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

    fun findPost(id: Long): Post? {
        return _data.value?.posts?.find {
            it.id==id
        }
    }

}