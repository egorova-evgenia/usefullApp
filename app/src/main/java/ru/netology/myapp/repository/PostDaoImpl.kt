package ru.netology.myapp.repository

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import ru.netology.myapp.dto.Post
import ru.netology.myapp.viewmodel.newPostId

interface PostDao {
    fun getAll(): List<Post>
    fun likeById(id: Int)
    fun shareById(id:Int)
    fun removeById(id:Int)
    fun save(post: Post): Post}

class PostDaoImpl(private val db: SQLiteDatabase): PostDao {
    object PostColumns{
        const val TABLE ="posts"
        const val COLUMN_ID= "id"
        const val COLUMN_AUTOR="autor"
        const val COLUMN_AUTOR_AVATAR="autorAvatar"
        const val COLUMN_PUBLISHED="published"
        const val COLUMN_CONTENT="content"
        const val COLUMN_URL="url"
        const val COLUMN_LIKES="likes"
        const val COLUMN_SHARES="shares"
        const val COLUMN_VIEWES="viewes"
        const val COLUMN_I_LIKED="iLiked"
        val ALL_COLUMNS=arrayOf(
            COLUMN_ID,
            COLUMN_AUTOR,
            COLUMN_AUTOR_AVATAR,
            COLUMN_PUBLISHED,
            COLUMN_CONTENT,
            COLUMN_URL,
            COLUMN_LIKES,
            COLUMN_SHARES,
            COLUMN_VIEWES,
            COLUMN_I_LIKED
        )
    }


    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun likeById(id: Int) {
        db.execSQL(
            """
           UPDATE posts SET
               likes = likes + CASE WHEN iLiked THEN -1 ELSE 1 END,
               iLiked = CASE WHEN iLiked THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun shareById(id: Int) {
        db.execSQL(
            """
           UPDATE posts SET
               shares = shares + 1 
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Int) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            if (post.id != newPostId) {
                put(PostColumns.COLUMN_ID,post.id)
            }
            put(PostColumns.COLUMN_AUTOR, "Mary")
            put(PostColumns.COLUMN_AUTOR_AVATAR, "@tools:sample/avatars")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_PUBLISHED, "now")
            put(PostColumns.COLUMN_URL, "url")
        }
        val id =db.replace(PostColumns.TABLE, null, values)
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(post.id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
            id = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
            autor= getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTOR)),
            autorAvatar = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTOR_AVATAR)),
            published  =  getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
            content=getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
            url =getString(getColumnIndexOrThrow(PostColumns.COLUMN_URL)),
            likes=getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
            shares=getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARES)),
            viewes=getInt(getColumnIndexOrThrow(PostColumns.COLUMN_VIEWES)),
            iLiked=getInt(getColumnIndexOrThrow(PostColumns.COLUMN_I_LIKED)) !=0
            )
        }
    }

    companion object{
        val DDL = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_AUTOR_AVATAR} TEXT,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_URL} TEXT,
            ${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIEWES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_I_LIKED} BOOLEAN NOT NULL DEFAULT 0,
        );
        """.trimIndent()}
}