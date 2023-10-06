package ru.netology.myapp.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.myapp.dto.Attachment
import ru.netology.myapp.dto.Event

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val datetime: String,
    val content: String,
    val published: String,
//    @Embedded //??? вложенность
//    val coords: String?,
//    val type: EventType,

//    val likeOwnerIds:List<Int>?,  //String?
    var likedByMe: Boolean,
//    val speakerIds: List<Int>? = null,
    val participatedByMe: Boolean,
    @Embedded
    val attachment: Attachment?,
    val link: String?,
    val ownedByMe: Boolean,
//    @Embedded
//    val users: List<UserPreview>?,//todo что это???

//    todo понадобится для crud
    val isRemoteSaved: Boolean = false,
) {
    fun toEventDto(): Event =
        Event(
            id = id,
            authorId = authorId,
            author = author,
            authorAvatar = authorAvatar,
            authorJob = authorJob,
            datetime = datetime,
            content = content,
            published = published,
            likedByMe = likedByMe,
            participatedByMe = participatedByMe,
            attachment = attachment,
            link = link,
            ownedByMe = ownedByMe,
        )

    companion object {
        fun fromDto(dto: Event) =
            EventEntity(
                dto.id, dto.authorId, dto.author, dto.authorAvatar,
                dto.authorJob, dto.datetime, dto.content, dto.published,
                dto.likedByMe, dto.participatedByMe, dto.attachment,
                dto.link, dto.ownedByMe
//                dto.likeOwnerIds,
//                dto.likeOwnerIds?.joinToString(","),

            )
    }
}


fun List<EventEntity>.toDto(): List<Event> = map(EventEntity::toEventDto)
fun List<Event>.toEntity(): List<EventEntity> = map(EventEntity::fromDto)

