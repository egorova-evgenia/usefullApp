package ru.netology.myapp.models

import ru.netology.myapp.dto.Job

data class UserModel(
    val id: Int,
    val login: String,
    val name: String,
    val avatar: String?,
    val jobList: List<Job>,
    val ownedByMe: Boolean
)