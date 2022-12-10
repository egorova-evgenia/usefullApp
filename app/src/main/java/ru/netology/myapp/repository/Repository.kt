package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.myapp.dto.Post
import ru.netology.myapp.viewmodel.newPostId

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Int)
    fun shareById(id:Int)
    fun removeById(id:Int)
    fun save(post: Post)
}
class PostRepositoryInMemory: PostRepository {

    // Вот аналог PostRepositorySQLiteImpl
    private var posts = listOf(Post(
            id = 0,
            autor = "Autor_Name. Длииииииииииииииииииииииииииииииииииииинно",
            autorAvatar = "@tools:sample/avatars",
            published = "30 июля в 10.30",
            content = "короткий текст" +
                    "короткий текст",
            url = "www.url.ru",
            likes = 0,
            shares = 999,
            viewes = 22
    ),
        Post(
            id = 1,
            autor = "Name",
            autorAvatar = "@tools:sample/avatars",
            published = "30 июля в 10.30",
            content = "When a view's measure() method returns, its getMeasuredWidth() and getMeasuredHeight() values must be set, along with those for all of that view's descendants. A view's measured width and measured height values must respect the constraints imposed by the view's parents. This guarantees that at the end of the measure pass, all parents accept all of their children's measurements. A parent view may call measure() more than once on its children. For example, the parent may measure each child once with unspecified dimensions to find out how big they want to be, then call measure() on them again with actual numbers if the sum of all the children's unconstrained sizes is too big or too small.",
            url = "www.url.ru",
            likes = 0,
            shares = 91,
            viewes = 212
        )
    )
    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Int) {
        posts=posts.map {
            if (it.id!=id) it else it.copy(iLiked = !it.iLiked,
        likes = if(it.iLiked) it.likes-1 else it.likes+1)
        }
        data.value=posts
    }

    override fun shareById(id: Int) {
        posts=posts.map {
            if (it.id!=id) it else it.copy(shares=it.shares+1)
        }
        data.value=posts
    }

    override fun removeById(id: Int) {
        posts=posts.filter {it.id!=id}
        data.value=posts
    }

    fun getNextId() = posts.size

    override fun save(post: Post) {
//        data.value =
//        posts=listOf(post.copy(id = posts.firstOrNull()?.id?:1)) + posts

        if    (post.id == newPostId) {
            posts=listOf(post.copy(id = getNextId())) + posts
        } else {
        posts=posts.map {
            if (it.id==post.id) it.copy(content = post.content) else it
        }
        }
        data.value = posts
    }
}
