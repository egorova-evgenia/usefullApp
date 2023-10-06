package ru.netology.myapp.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.myapp.dto.Event

interface EventRepository {
    val data: Flow<PagingData<Event>>
}