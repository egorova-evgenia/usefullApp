package ru.netology.myapp.adapter

import ru.netology.myapp.dto.Job

interface JobEventListener {
    fun onRemove(job: Job)
    fun onEdit(job: Job)
    fun onCancelEdit(job: Job)
}