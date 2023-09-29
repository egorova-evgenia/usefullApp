
package ru.netology.myapp.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.myapp.entity.PostEntity

@Dao
interface PostDao {
    //    @Query("SELECT * FROM PostEntity WHERE toShow=1 ORDER BY id DESC")
//    fun getAll(): Flow<List<PostEntity>>
    @Query("SELECT * FROM PostEntity WHERE id = :id")
    fun getById(id: Int): Flow<PostEntity?>

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getPagingSource(): PagingSource<Int, PostEntity>

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    suspend fun updateContentById(id: kotlin.Int, content: String)

    suspend fun save(post: PostEntity) =
        if (post.id == 0) insert(post) else updateContentById(post.id, post.content)


    //    todo плюс 1 лайк
//    likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
//    @Insert(""" )
    @Query(
        """
        UPDATE PostEntity SET
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE  id = :id
        """
    )
    suspend fun likeById(id: kotlin.Int)

//    @Query(
//        """
//    UPDATE PostEntity SET
//    toShow = 1
//    """
//    )
//    suspend fun toShowAll()//true

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: kotlin.Int)

    @Query("DELETE FROM PostEntity")
    suspend fun removeAll()
}