package ru.netology.myapp.viewmodel

import ru.netology.myapp.dto.Post

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val empty: Boolean = false,
)

data class FeedModelState (
    val loading: Boolean = false,
    val error: Boolean = false,
    val swiprefresh: Boolean = false,
//    val code: Int = 200,
//    val errorBody: String = ""
)
