package ru.netology.myapp.repository.db_sql

import DbHelper
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ru.netology.myapp.repository.PostDao
import ru.netology.myapp.repository.PostDaoImp

class AppDb private constructor(db: SQLiteDatabase){
    val postDao: PostDao = PostDaoImp(db)

    companion object {
        @Volatile
        private var instance: AppDb? = null
        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this){
                instance ?: AppDb(
                    buildDatabase(context, arrayOf(PostDaoImp.DDL))
                ).also { instance = it }
            }
        }
        private fun buildDatabase(context: Context, DDLs: Array<String>) = DbHelper(
            context, 1, "app.db", DDLs,
        ).writableDatabase
    }
}