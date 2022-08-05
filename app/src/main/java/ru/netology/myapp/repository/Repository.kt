package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.myapp.dto.Post

interface PostRepository {
    fun get(): LiveData<Post>
    fun like()
    fun share()
}
class PostRepositoryInMemory: PostRepository {
    private var post = Post(
            id = 0,
            autor = "Netology. Длииииииииииииииииииииииииииииииииииииинно",
            autorAvatar = "@tools:sample/avatars",
            published = "30 июля в 10.30",
            content = "When a view's measure() method returns, its getMeasuredWidth() and getMeasuredHeight() values must be set, along with those for all of that view's descendants. A view's measured width and measured height values must respect the constraints imposed by the view's parents. This guarantees that at the end of the measure pass, all parents accept all of their children's measurements. A parent view may call measure() more than once on its children. For example, the parent may measure each child once with unspecified dimensions to find out how big they want to be, then call measure() on them again with actual numbers if the sum of all the children's unconstrained sizes is too big or too small.",
            url = "www.url.ru",
            likes = 0,
            shares = 999,
            viewes = 22
    )
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        post=post.copy(iLiked = !post.iLiked,
        likes = if(post.iLiked) post.likes+1 else post.likes-1)
        data.value=post
    }

    override fun share() {
        post=post.copy(shares=post.shares++)
    }

}
