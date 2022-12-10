package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.myapp.dto.Post
import ru.netology.myapp.viewmodel.newPostId

class PostRepositorySQLiteImpl(private val dao: PostDao):PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Int) {
        dao.likeById(id)
        data.value=posts
    }

    override fun shareById(id: Int) {
        dao.shareById(id)
        data.value=posts    }

    override fun removeById(id: Int) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts    }

    fun getNextId() = posts.size

    override fun save(post: Post) {
        val saved = dao.save(post)
        posts= if(post.id == newPostId) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id==post.id) saved else it
            }
        }
        data.value = posts
    }
}