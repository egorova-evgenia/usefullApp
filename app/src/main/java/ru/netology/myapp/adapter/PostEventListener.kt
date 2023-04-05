package ru.netology.myapp.adapter

import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import ru.netology.myapp.FeedFragment.Companion.textArg
import ru.netology.myapp.R
import ru.netology.myapp.dto.Post

interface PostEventListener{
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onCancelEdit(post: Post)
    fun onShowOnePost(post: Post)
//    fun refresh()
}
