package ru.netology.myapp.viewmodel

import retrofit2.http.Body
import ru.netology.myapp.dto.Post

data class FeedModel(
    val posts: List<Post> = emptyList(),
//    val post: Post? ,
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = false,
    val swiprefresh: Boolean = false,
    val code: Int = 200,
    val errorBody: String = ""
)
