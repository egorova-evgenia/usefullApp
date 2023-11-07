package ru.netology.myapp.dto

sealed interface FeedItem {
    val id: Int
}

data class Post(
    override val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
    val coords: Coordinates? = null,
    val link: String?,
    val likeOwnerIds: List<Int>,//
    val mentionIds: List<Int>,//
    val mentionMe: Boolean,
    val likedByMe: Boolean,
    val attachment: Attachment? = null,
    val ownedByMe: Boolean,
    val users: Map<Int, UserPreview>? = null//
) : FeedItem

data class Coordinates(
    val lat: Double,
    val long: Double,
)

data class UserPreview(
    val name: String,
    val avatar: String?
)

data class Attachment(
    val url: String,
    val type: AttachmentType,
)

enum class AttachmentType {
    IMAGE, VIDEO, AUDIO
}

data class Ad(
    override val id: Int,
    val image: String,
) : FeedItem


