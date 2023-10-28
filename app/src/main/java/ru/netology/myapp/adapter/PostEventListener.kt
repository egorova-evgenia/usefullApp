package ru.netology.myapp.adapter

import ru.netology.myapp.dto.Post

interface PostEventListener{
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onCancelEdit(post: Post)
    fun onShowOnePost(post: Post)
    fun onShowOneImage(post: Post)//    fun refresh()
    fun onShowVideoFragment(post: Post)
    fun onControlAudio(post: Post)
    fun onShowUser(post: Post)
}
