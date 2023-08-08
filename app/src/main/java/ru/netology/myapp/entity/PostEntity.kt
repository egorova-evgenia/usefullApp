package ru.netology.myapp.entity
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.myapp.dto.Attachment
import ru.netology.myapp.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String = "",
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val authorId: Long,
    @Embedded
    val attachment: Attachment? = null,
    val isRemoteSaved: Boolean = false,
    var toShow: Boolean?=null,//по умолчанию не видно
    ) {
    fun toDto() =
        Post(id, author, authorAvatar, content, published, likedByMe, likes,attachment, authorId, )
    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.author,
                dto.authorAvatar,
                dto.content,
                dto.published,
                dto.likedByMe,
                dto.likes,
                dto.authorId,
                dto.attachment,

            )
    }
}
fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)

