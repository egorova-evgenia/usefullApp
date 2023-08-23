package ru.netology.myapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.myapp.dao.PostDao
import ru.netology.myapp.dao.PostRemoteKeyDao
import ru.netology.myapp.entity.PostEntity
import ru.netology.myapp.entity.PostRemoteKeyEntity

@Database(entities = [PostEntity::class, PostRemoteKeyEntity::class], version = 1)

abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
}