package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.myapp.dto.Post
import ru.netology.myapp.exceptions.PostNotFoundException
import ru.netology.myapp.viewmodel.newPostId

class PostRepositorySQLImp (private val dao: PostDao): PostRepository {

    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts =dao.getAll()
        data.value =posts
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Int) {
        dao.likeById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                iLiked = !it.iLiked,
                likes = if (it.iLiked) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Int) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shares = it.shares + 1)
        }
        data.value = posts
    }

    override fun removeById(id: Int) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        val id = post.id
        val saved=dao.save(post)

        posts = if(id == newPostId) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id==post.id) saved else it
            }
        }
        data.value = posts

    }

    override fun findPost(id: Int): Post {
        val pst = posts.find {it.id==id}
            if (pst != null) {
                return pst
            }
        else {throw PostNotFoundException("Пост не найден")
        }
    }

    override fun filterPost(id: Int): List<Post> = posts.filter {it.id==id}
}