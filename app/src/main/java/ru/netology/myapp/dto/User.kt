package ru.netology.myapp.dto

import kotlinx.coroutines.flow.Flow

data class User(
    val id: Int,
    val login: String,
    val name: String,
    val avatar: String?
)

data class Job(
    val id: Int,
    val name: String,
    val position: String,
    val start: String,
    val finish: String?,
    val link: String?,

    val ownedByMe: Boolean
)
