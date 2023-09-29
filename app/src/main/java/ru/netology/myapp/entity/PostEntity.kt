package ru.netology.myapp.entity
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.netology.myapp.dto.Attachment
import ru.netology.myapp.dto.Post

@Entity
//@TypeConverters(PostConverter::class)
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
//    @Embedded //??? вложенность

//    val coords: String?,
    val link: String?,
//    @TypeConverter
//    @Embedded
//    val likeOwnerIds:String? ,  //List<Int>?
//    @Embedded
//    val mentionIds: List<Int>,
    val mentionMe: Boolean,
    var likedByMe: Boolean,
    @Embedded
    val attachment: Attachment?,
    val ownedByMe: Boolean,
//    @Embedded
//    val users: List<UserPreview>?,//todo что это???
    val isRemoteSaved: Boolean = false,
) {
    fun toDto() =
        Post(
            id = id,
            authorId = authorId,
            author = author,
            authorAvatar = authorAvatar,
            authorJob = authorJob,
            content = content,
            published = published,
            link = link,
//            likeOwnerIds= likeOwnerIds?.let { toListInt(it) },
            mentionMe = mentionMe,
            likedByMe = likedByMe,
            ownedByMe = ownedByMe
        )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id, dto.authorId, dto.author, dto.authorAvatar,
                dto.authorJob, dto.content, dto.published,

                dto.link,
//                dto.likeOwnerIds?.joinToString(","),
                dto.mentionMe,
                dto.likedByMe, dto.attachment, dto.ownedByMe
            )
    }
}


fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)


