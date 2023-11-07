package ru.netology.myapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.myapp.auth.AppAuth
import ru.netology.myapp.dto.FeedItem
import ru.netology.myapp.dto.Post
import ru.netology.myapp.eventsAndOther.SingleLiveEvent
import ru.netology.myapp.repository.PostRepository
import javax.inject.Inject

val newPostId = 0
val empty = Post(
    newPostId,
    0,
    "Mary",
    "  ",
    " ",
    "1",
    "",
    null,
    null,
    listOf<Int>(),
    listOf<Int>(),
    false,
    false,
    null,
    false,
    null,
)

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    appAuth: AppAuth,
) : ViewModel() {
    private val scope = MainScope()

//    private val cached = repository.dataToShow.cachedIn(scope)

    val postsData: Flow<PagingData<FeedItem>> = appAuth
        .authStateFlow.flatMapLatest { (myId, _) ->
            repository.data.map { posts ->
                posts.map { post ->
                    if (post is Post) {
                        post.copy(ownedByMe = (post.authorId == myId))
                    } else {
                        post
                    }
                }
            }
        }



    private val _dataState = MutableLiveData(FeedModelState())
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    init {
        loadPosts()
    }

    fun loadPosts() = scope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }


    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    private val _postCreated = SingleLiveEvent<Unit>()

    //    в editFrsgment
    val postCreated: LiveData<Unit>
        get() = _postCreated

//    val newerCount: LiveData<Int> = data.switchMap {
//        val id=it.posts.firstOrNull()?.id ?: 0L
//        repository.getNewer(id)
//            .asLiveData(Dispatchers.Default)
//    }


    val edited = MutableLiveData(empty)

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

    fun shareById(id: Int) {
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

    fun changeHidden() {
        scope.launch {
            try {
                repository.changeHidden()
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

    private val _attachmentState = MutableLiveData<AttachmentForSaving?>()
    val attachmentState: LiveData<AttachmentForSaving?>
        get() = _attachmentState

    fun changeAttachment(attachmentForSaving: AttachmentForSaving?) {
        _attachmentState.value = attachmentForSaving
    }

    fun save() {
        edited.value?.let { post ->
            _postCreated.value = Unit
            scope.launch {
                try {
                    when (_attachmentState.value) {
                        null -> repository.save(post)
                        else -> _attachmentState.value?.file.let { file ->
                            repository.saveWithAttachment(
                                post,
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

            edited.value = empty
        }
    }


    fun edit(post: Post) {
        edited.value = post
    }

    fun cancelEdit() {
        edited.value = empty // сброс текста
    }

    fun getPostById(id: Int): LiveData<Post?> = repository.getItemById(id)

}