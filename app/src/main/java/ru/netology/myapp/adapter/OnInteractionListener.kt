package ru.netology.myapp.adapter

import ru.netology.myapp.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post){}
    fun onShare(post: Post){}
    fun onEdit(post: Post){}
    fun onRemove(post: Post){}

}