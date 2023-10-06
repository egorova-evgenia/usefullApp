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
import ru.netology.myapp.repository.PostRepository
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: PostRepository,
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


}