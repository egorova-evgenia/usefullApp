package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.myapp.dao.PostDao
import ru.netology.myapp.dto.Post
import ru.netology.myapp.entity.PostEntity
import ru.netology.myapp.exceptions.PostNotFoundException
import ru.netology.myapp.viewmodel.newPostId

class PostRepositoryImp(private val dao: PostDao
):PostRepository {

    override fun getAll() = dao.getAll().map {
        it.map(PostEntity::toDto)
    }

    override fun likeById(id: Int) {
        dao.likeById(id)
//            data.value=posts
    }

    override fun shareById(id: Int) {
        dao.shareById(id)
//            data.value=posts
    }

    override fun removeById(id: Int) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
        val saved = dao.save(PostEntity.fromDto(post))
    }

    override fun findPost(id: Int): Post = dao.findPost(id).toDto()
}