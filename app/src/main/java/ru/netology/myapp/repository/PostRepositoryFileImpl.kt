//package ru.netology.myapp.repository
//
//import android.content.Context
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import ru.netology.myapp.dto.Post
//
//class PostRepositoryFileImpl(private val context: Context): PostRepository {
//    private val gson= Gson()
//    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
//    private var newPostId =-1
//    private var posts = emptyList<Post>()
//    private val data = MutableLiveData(posts)
//    private val filename = "posts.json"
//
//    init {
//        val file = context.filesDir.resolve(filename)
//        if (file.exists()) {
//            context.openFileInput(filename).bufferedReader().use {
//                posts = gson.fromJson(it, type)
//                data.value = posts
//            }
//        } else {
//            sync()
//        }
//    }
//
//    /// через ну может быть потоки
//    private fun sync() {
//        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
//            it.write(gson.toJson(posts))
//        }
//    }
//    override fun getAll(): LiveData<List<Post>> = data
//
//    override fun likeById(id: Int) {
//        posts=posts.map {
//            if (it.id!=id) it else it.copy(iLiked = !it.iLiked,
//                likes = if(it.iLiked) it.likes-1 else it.likes+1)
//        }
//        data.value=posts
//        sync()
//    }
//
//    override fun shareById(id: Int) {
//        posts=posts.map {
//            if (it.id!=id) it else it.copy(shares=it.shares+1)
//        }
//        data.value=posts
//        sync()
//    }
//
//    override fun removeById(id: Int) {
//        posts=posts.filter {it.id!=id}
//        data.value=posts
//        sync()
//    }
//
//    fun getNextId() = posts.size
//
//    override fun save(post: Post) {
//
//        if    (post.id == newPostId) {
//            posts=listOf(post.copy(id = getNextId())) + posts
//        } else {
//            posts=posts.map {
//                if (it.id==post.id) it.copy(content = post.content) else it
//            }
//        }
//        data.value = posts
//        sync()
//    }
//}