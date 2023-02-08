package ru.netology.myapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.myapp.dto.Post
import ru.netology.myapp.entity.PostEntity
import ru.netology.myapp.viewmodel.newPostId

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    fun updateContentById(id: Int, content: String)

    fun save(post: PostEntity) =
        if (post.id == newPostId) insert(post) else updateContentById(post.id, post.content)

    @Query("""
        UPDATE PostEntity SET
        likes = likes + CASE WHEN iliked THEN -1 ELSE 1 END,
        iliked = CASE WHEN iliked THEN 0 ELSE 1 END
        WHERE id = :id
        """)

    fun likeById(id: Int)
    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Int)
    @Query("""
        UPDATE PostEntity SET
        shares = shares + 1
        WHERE id = :id
        """)
    fun shareById(id: Int)

    @Query("SELECT * FROM PostEntity WHERE id = :id")
    fun findPost(id: Int): PostEntity
}