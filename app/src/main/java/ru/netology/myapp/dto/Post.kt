package ru.netology.myapp.dto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.myapp.enumeration.AttachmentType

data class Post(
        val id: Long,
        val author: String,
        val authorAvatar: String = "",
        val content: String,
        val published: Long,
        val likedByMe: Boolean,
        val likes: Int = 0,
        val attachment: Attachment? = null,
)
data class Attachment(
        val url: String,
        val description: String?,
        val type: AttachmentType,
)
enum class AttachmentType {
        IMAGE
}
