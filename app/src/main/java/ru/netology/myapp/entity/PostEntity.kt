package ru.netology.myapp.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.myapp.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var author: String,
    var content: String,
    var published: Long,
    var likedByMe: Boolean,
    var likes: Int = 0,
){
    fun toDto(): Post = Post(id, author, content,published, likedByMe, likes  )

    companion object {
    fun fromDto(post: Post): PostEntity =
        PostEntity(post.id, post.author, post.content,post.published, post.likedByMe, post.likes)

    }
}