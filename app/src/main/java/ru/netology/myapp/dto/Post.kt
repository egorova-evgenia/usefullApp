package ru.netology.myapp.dto

sealed interface FeedItem {
    val id: Long
}

data class Post(
    override val id: Long,
    val author: String,
    val authorAvatar: String = "",
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val attachment: Attachment? = null,
    val authorId: Long,
    val ownedByMe: Boolean = false,
) : FeedItem

data class Attachment(
    val url: String,
    val description: String?,
    val type: AttachmentType,
)

enum class AttachmentType {
    IMAGE
}

data class Ad(
    override val id: Long,
    val image: String,
) : FeedItem
