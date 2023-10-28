package ru.netology.myapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.myapp.auth.AppAuth
import ru.netology.myapp.dto.FeedItem
import ru.netology.myapp.dto.Event
import ru.netology.myapp.dto.EventType
import ru.netology.myapp.dto.Post
import ru.netology.myapp.eventsAndOther.SingleLiveEvent
import ru.netology.myapp.repository.EventRepository
import ru.netology.myapp.repository.PostRepository
import javax.inject.Inject

val newEventId = 0
val emptyEvent = Event(
    newPostId,
    0,
    "Mary",
    "  ",
    " ",
    "1",
    "",
    "",
    null,
    EventType.ONLINE,
    null,
    false,
    null,
    false,
    null,
    null,
    false,
    null,
)

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository,
    appAuth: AppAuth,
) : ViewModel() {
    private val scope = MainScope()

    val eventsData: Flow<PagingData<FeedItem>> = appAuth
        .authStateFlow.flatMapLatest { (myId, _) ->
            repository.data.map { posts ->
                posts.map { it ->
                    if (it is Event) {
                        it.copy(ownedByMe = (it.authorId == myId))
                    } else {
                        it
                    }
                }
            }
        }

    private val _dataState = MutableLiveData(FeedModelState())
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    init {
        loadEvents()
    }

    fun loadEvents() = scope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            _dataState.value = FeedModelState()// обновляем MutableLiveData
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }


    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    private val _eventCreated = SingleLiveEvent<Unit>()
    val eventCreated: LiveData<Unit>
        get() = _eventCreated

    val edited = MutableLiveData(emptyEvent)

    fun likeById(id: Int) {
        scope.launch {
            try {
                repository.likeById(id)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun disLikeById(id: Int) {
        scope.launch {
            try {
                repository.disLikeById(id)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun removeById(id: Int) {
        scope.launch {
            try {
                repository.removeById(id)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun editContent(content: String) {
        edited.value?.let {
            val trimmed = content.trim()
            if (trimmed == it.content) {
                return
            }
            edited.value = it.copy(content = trimmed)
        }
    }

    private val _attachmentState = MutableLiveData<AttachmentModel?>()
    val attachmentState: LiveData<AttachmentModel?>
        get() = _attachmentState

    fun changeAttachment(attachmentModel: AttachmentModel?) {
        _attachmentState.value = attachmentModel
    }

    fun save() {
        edited.value?.let { it ->
            _eventCreated.value = Unit
            scope.launch {
                try {
                    when (_attachmentState.value) {
                        null -> repository.save(it)
                        else -> _attachmentState.value?.file.let { file ->
                            repository.saveWithAttachment(
                                it,
                                _attachmentState.value!!,
                                _attachmentState.value?.type
                            )
                        }

                    }
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }

            edited.value = emptyEvent
        }
    }

    fun edit(event: Event) {
        edited.value = event
    }

    fun cancelEdit() {
        edited.value = emptyEvent
    }

    fun getEventById(id: Int): LiveData<Event?> = repository.getItemById(id)


}