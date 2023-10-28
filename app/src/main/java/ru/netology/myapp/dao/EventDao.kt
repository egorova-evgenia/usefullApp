package ru.netology.myapp.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.myapp.entity.EventEntity
import ru.netology.myapp.entity.PostEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM EventEntity ORDER BY id DESC")
    fun getPagingSource(): PagingSource<Int, EventEntity>

    @Query("SELECT COUNT(*) == 0 FROM EventEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(events: List<EventEntity>)

    //        to do сделать как-то так likeOwnerIds = :likeOwners,
    @Query(
        """
        UPDATE EventEntity SET
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE  id = :id
        """
    )
    suspend fun likeById(id: Int)

    @Query("DELETE FROM EventEntity WHERE id = :id")
    suspend fun removeById(id: Int)

    @Query("DELETE FROM EventEntity")
    suspend fun removeAll()

    @Query("SELECT * FROM EventEntity WHERE id = :id")
    fun getById(id: Int): Flow<EventEntity?>


}