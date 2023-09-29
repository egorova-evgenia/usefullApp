package ru.netology.myapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.myapp.entity.PostRemoteKeyEntity

@Dao
interface PostRemoteKeyDao {
    @Query("SELECT COUNT(*) == 0 FROM PostRemoteKeyEntity")
    suspend fun isEmpty(): Boolean

    @Query("SELECT max(key) FROM PostRemoteKeyEntity")
    suspend fun max(): Int?

    @Query("SELECT min(key) FROM PostRemoteKeyEntity")
    suspend fun min(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(key: PostRemoteKeyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: List<PostRemoteKeyEntity>)

    @Query("DELETE FROM PostRemoteKeyEntity")
    suspend fun clear()
}