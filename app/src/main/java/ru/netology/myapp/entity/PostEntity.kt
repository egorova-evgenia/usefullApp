package ru.netology.myapp.entity
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import ru.netology.myapp.dto.Attachment
import ru.netology.myapp.dto.Coordinates
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
    val coords: Coordinates?,
    val link: String?,
    val likeOwnerIds: List<Int>,  //String?
    val mentionIds: List<Int>,
    val mentionMe: Boolean,
    var likedByMe: Boolean,
    @Embedded
    val attachment: Attachment?,
    val ownedByMe: Boolean,
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
            coords = coords,
            link = link,
            likeOwnerIds = likeOwnerIds,
            mentionMe = mentionMe,
            mentionIds = mentionIds,
            likedByMe = likedByMe,
            attachment = attachment,
            ownedByMe = ownedByMe
        )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id, dto.authorId, dto.author, dto.authorAvatar,
                dto.authorJob, dto.content, dto.published,
                dto.coords, dto.link,
                dto.likeOwnerIds, dto.mentionIds,
                dto.mentionMe,
                dto.likedByMe, dto.attachment, dto.ownedByMe
            )
    }
}


fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)

// типичный ответ сервера
//{"id": 2218, "authorId": 517, "author": "test3", "authorAvatar": null, "authorJob": "sdfsdf", "content": "dfsdfsdf sdfsdf", "published": "2023-09-25T20:47:29.294933Z", "coords": null, "link": null, "likeOwnerIds": [], "mentionIds": [], "mentionedMe": false, "likedByMe": false, "attachment": null, "ownedByMe": false, "users": {}}