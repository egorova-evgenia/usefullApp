package ru.netology.myapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.myapp.entity.PostRemoteKeyEntity

@Dao
interface PostRemoteKeyDao {

    @Query("SELECT MAX('key') FROM PostRemoteKeyEntity")
    suspend fun max(): Long?

    @Query("SELECT MIN('key') FROM PostRemoteKeyEntity")
    suspend fun min(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(key: PostRemoteKeyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: List<PostRemoteKeyEntity>)

//    Удаление при рефреше
//    @Query("DELETE FROM PostRemoteKeyEntity")
//    suspend fun removeAll()
}