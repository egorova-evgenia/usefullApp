package ru.netology.myapp.models

import ru.netology.myapp.dto.Attachment
import ru.netology.myapp.dto.AttachmentType
import ru.netology.myapp.dto.Coordinates
import ru.netology.myapp.dto.Post
import ru.netology.myapp.dto.UserPreview

sealed interface FeedItemModel {
    val id: Int
}

data class PostModel(
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
    val attachmentModel: AttachmentModel? = null,
    val ownedByMe: Boolean,
    val users: Map<Int, UserPreview>? = null,
    val likes: Int
) : FeedItemModel

fun Post.toModel() = this.let {
    PostModel(
        id = this.id,
        authorId = this.authorId,
        author = this.author,
        authorAvatar = this.authorAvatar,
        authorJob = this.authorJob,
        content = this.content,
        published = this.published,
        coords = this.coords,
        link = this.link,
        likeOwnerIds = this.likeOwnerIds,
        mentionMe = this.mentionMe,
        mentionIds = this.mentionIds,
        likedByMe = this.likedByMe,
        attachmentModel = attachment.toModel(),
        ownedByMe = this.ownedByMe,
        likes = this.likeOwnerIds.size
    )
}

fun PostModel.toPost() = this.let {
    Post(
        id = this.id,
        authorId = this.authorId,
        author = this.author,
        authorAvatar = this.authorAvatar,
        authorJob = this.authorJob,
        content = this.content,
        published = this.published,
        coords = this.coords,
        link = this.link,
        likeOwnerIds = this.likeOwnerIds,
        mentionMe = this.mentionMe,
        mentionIds = this.mentionIds,
        likedByMe = this.likedByMe,
        attachment = attachmentModel.toAttachment(),
        ownedByMe = this.ownedByMe,
    )
}

data class AttachmentModel(
    val url: String,
    val type: AttachmentType,
    val isPlaying: Boolean? = null,
)

fun Attachment?.toModel() = this?.let {
    AttachmentModel(
        url = this.url,
        type = this.type,
        isPlaying = false,
    )
}

fun AttachmentModel?.toAttachment() = this?.let {
    Attachment(
        url = this.url,
        type = this.type,
    )
}