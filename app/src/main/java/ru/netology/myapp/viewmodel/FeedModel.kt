package ru.netology.myapp.viewmodel

import ru.netology.myapp.dto.Post

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = false,
    val swiprefresh: Boolean = false,
)
