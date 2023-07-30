package ru.netology.myapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.myapp.auth.AppAuth
import ru.netology.myapp.dto.Post
import ru.netology.myapp.eventsAndOther.SingleLiveEvent
import ru.netology.myapp.repository.PostRepository
import javax.inject.Inject

val newPostId=0L
val empty = Post(
    newPostId,
    "Mary",
    "  ",
    " ",
    1,
    false,
    0,
    authorId = 0L
)

//@AndroidEntryPoint
@HiltViewModel
@ExperimentalCoroutinesApi
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    appAuth: AppAuth,
) : ViewModel() {
    private val scope = MainScope()//page 14

    val dataToShow: Flow<PagingData<Post>> = appAuth
        .authStateFlow.flatMapLatest { (myId, _) ->
            repository.dataToShow.map { posts ->
                posts.map { post ->
                    post.copy(ownedByMe = (post.authorId == myId))
                }
            }
        }.flowOn(Dispatchers.Default)

    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel> = appAuth
        .authStateFlow.flatMapLatest { (myId, _) ->
            repository.data.map { posts ->
                FeedModel(posts.map { post ->
                    post.copy(ownedByMe = post.authorId == myId)
                }, posts.isEmpty())
            }
        }.asLiveData(Dispatchers.Default)


    private val _dataState =MutableLiveData(FeedModelState())
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    init {
        loadPosts()
    }

    fun loadPosts() = scope.launch {// корутина
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()// обновляем MutableLiveData
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }


    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }


    fun findPost(id: Long): Post? {
        return _data.value?.posts?.find {
            it.id==id
        }
    }

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    val newerCount: LiveData<Int> = data.switchMap {
        val id=it.posts.firstOrNull()?.id ?: 0L
        repository.getNewer(id)
            .asLiveData(Dispatchers.Default)
    }


    val edited = MutableLiveData(empty)

    fun likeById(id: Long) {
        scope.launch {
            try {
                repository.likeById(id)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun disLikeById(id: Long) {
        scope.launch {
            try {
                repository.disLikeById(id)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }
        fun shareById(id: Long) {
    }
    fun removeById(id: Long) {
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
        if (trimmed==it.content){
            return}
        edited.value=it.copy(content=trimmed)
        }
    }

    private val _photoState = MutableLiveData<PhotoModel?>()
    val photoState: LiveData<PhotoModel?>
        get()=_photoState
    fun changePhoto(photoModel: PhotoModel?) {
        _photoState.value=photoModel
    }

    fun save() {
        edited.value?.let { post ->
            _postCreated.value = Unit
            scope.launch {
                try {
                    when(_photoState.value) {
                        null -> repository.save(post)
                        else -> _photoState.value?.file.let{file ->
                            repository.saveWithAttachment(post, _photoState.value!!)}

                    }
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }

            edited.value = empty
        }
    }


    fun edit(post: Post){
        edited.value =post
    }

    fun  cancelEdit(){
        edited.value =empty // сброс текста
    }

    fun refresh(){
        loadPosts()
    }


}