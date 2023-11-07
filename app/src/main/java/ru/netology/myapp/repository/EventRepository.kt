package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.myapp.dto.AttachmentType
import ru.netology.myapp.dto.Event
import ru.netology.myapp.viewmodel.AttachmentForSaving

interface EventRepository {
    val data: Flow<PagingData<Event>>

    suspend fun likeById(id: Int)
    suspend fun disLikeById(id: Int)
    suspend fun removeById(id: Int)
    fun getItemById(id: Int): LiveData<Event?>
    suspend fun save(event: Event)
    suspend fun saveWithAttachment(
        event: Event,
        attachItem: AttachmentForSaving,
        attachType: AttachmentType?
    )
}