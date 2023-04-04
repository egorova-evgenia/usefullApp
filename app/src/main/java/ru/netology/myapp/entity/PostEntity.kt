package ru.netology.myapp.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.myapp.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val autor: String,
    val autorAvatar: String,
    val published: String,
    val content: String,
    val url: String,
    var likes: Int,
    var shares: Int,
    var viewes: Int,
    var iLiked: Boolean = false
){
    fun toDto(): Post = Post(id, autor, autorAvatar, published, content, url,  likes, shares, viewes, iLiked)

    companion object {
    fun fromDto(post: Post): PostEntity =
        PostEntity(post.id, post.autor, post.autorAvatar, post.published, post.content, post.url,  post.likes, post.shares, post.viewes, post.iLiked)

    }
}