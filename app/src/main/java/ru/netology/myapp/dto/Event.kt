package ru.netology.myapp.dto

data class Event(
    override val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val datetime: String,
    val published: String,
    val coords: Coordinates? = null,
    val type: EventType = EventType.OFFLINE,//
    val likeOwnerIds: List<Int>? = null,//
    val likedByMe: Boolean,
    val speakerIds: List<Int>? = null,//
    val participatedByMe: Boolean,
    val attachment: Attachment? = null,
    val link: String?,
    val ownedByMe: Boolean,
    val users: Map<Int, UserPreview>? = null//
) : FeedItem

enum class EventType {
    OFFLINE, ONLINE
}
