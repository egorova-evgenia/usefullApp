//package ru.netology.myapp.repository
//
//import android.content.ContentValues
//import android.database.Cursor
//import android.database.sqlite.SQLiteDatabase
//import android.os.Parcel
//import android.os.Parcelable
//import ru.netology.myapp.dao.PostDao
//import ru.netology.myapp.dto.Post
//import ru.netology.myapp.entity.PostEntity
//import ru.netology.myapp.viewmodel.newPostId
//
//class PostDaoImp(private val db: SQLiteDatabase): PostDao {
//
//    object PostColumns{
//        const val TABLE ="posts"
//        const val COLUMN_ID="id"
//        const val COLUMN_AUTOR="autor"
//        const val COLUMN_AUTORAVATAR="autorAvatar"
//        const val COLUMN_PUBLISHED="published"
//        const val COLUMN_CONTENT="content"
//        const val COLUMN_URL="url"
//        const val COLUMN_LIKES="likes"
//        const val COLUMN_SHARES="shares"
//        const val COLUMN_VIEWES="viewes"
//        const val COLUMN_ILIKES="iLiked"
//        val ALL_COLUMNS = arrayOf(
//            COLUMN_ID,
//            COLUMN_AUTOR,
//            COLUMN_AUTORAVATAR,
//            COLUMN_PUBLISHED,
//            COLUMN_CONTENT,
//            COLUMN_URL,
//            COLUMN_LIKES,
//            COLUMN_SHARES,
//            COLUMN_VIEWES,
//            COLUMN_ILIKES
//        )
//    }
//
//    companion object {
//        val DDL = """
//        CREATE TABLE ${PostColumns.TABLE} (
//            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
//            ${PostColumns.COLUMN_AUTOR} TEXT NOT NULL,
//            ${PostColumns.COLUMN_AUTORAVATAR} TEXT,
//            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
//            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
//            ${PostColumns.COLUMN_URL} TEXT,
//            ${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
//            ${PostColumns.COLUMN_SHARES} INTEGER NOT NULL DEFAULT 0,
//            ${PostColumns.COLUMN_VIEWES} INTEGER NOT NULL DEFAULT 0,
//            ${PostColumns.COLUMN_ILIKES} BOOLEAN NOT NULL DEFAULT 0
//        );
//        """.trimIndent()
//    }
//
//    override fun getAll(): List<Post> {
//        val posts = mutableListOf<Post>()
//        db.query(
//            PostColumns.TABLE,
//            PostColumns.ALL_COLUMNS,
//            null,
//            null,
//            null,
//            null,
//            "${PostColumns.COLUMN_ID} DESC"
//        ).use {
//            while (it.moveToNext()) {
//                posts.add(map(it))
//            }
//        }
//        return posts
//    }
//
//    override fun insert(post: PostEntity) {
//        TODO("Not yet implemented")
//    }
//
//    override fun updateContentById(id: Int, content: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun likeById(id: Int) {
//        db.execSQL(
//            """
//           UPDATE posts SET
//               likes = likes + CASE WHEN iLiked THEN -1 ELSE 1 END,
//               iLiked = CASE WHEN iLiked THEN 0 ELSE 1 END
//           WHERE id = ?;
//        """.trimIndent(), arrayOf(id)
//        )
//    }
//
//    override fun shareById(id: Int) {
//        db.execSQL(
//            """
//           UPDATE posts SET
//               shares = shares + 1
//           WHERE id = ?;
//        """.trimIndent(), arrayOf(id)
//        )
//    }
//
//    override fun findPost(id: Int): PostEntity {
//        TODO("Not yet implemented")
//    }
//
//    override fun removeById(id: Int) {
//        db.delete(
//            PostColumns.TABLE,
//            "${PostColumns.COLUMN_ID} = ?",
//            arrayOf(id.toString())
//        )
//    }
//
//    override fun save(post: Post): Post {
//        val values = ContentValues().apply {
//            if (post.id !=newPostId) {
//                put(PostColumns.COLUMN_ID, post.id)
//            }
//            put(PostColumns.COLUMN_AUTOR, post.autor)
//            put(PostColumns.COLUMN_AUTORAVATAR, post.autorAvatar)
//            put(PostColumns.COLUMN_PUBLISHED, post.published)
//            put(PostColumns.COLUMN_CONTENT, post.content)
//            put(PostColumns.COLUMN_URL, post.url)
//            put(PostColumns.COLUMN_LIKES, post.likes)
//            put(PostColumns.COLUMN_SHARES, post.shares)
//            put(PostColumns.COLUMN_VIEWES, post.viewes)
//            put(PostColumns.COLUMN_ILIKES, post.iLiked)
//        }
//
//        val id = db.replace(PostColumns.TABLE, null, values)
//        db.query(
//            PostColumns.TABLE,
//            PostColumns.ALL_COLUMNS,
//            "${PostColumns.COLUMN_ID} = ?",
//            arrayOf(id.toString()),
//            null,
//            null,
//            null,
//        ).use {
//            it.moveToNext()
//            return map(it)
//        }
//    }
//
//    private fun map(cursor: Cursor): Post {
//        with(cursor) {
//            return Post(
//                id = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
//                autor = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTOR)),
//                autorAvatar = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTORAVATAR)),
//                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
//                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
//                url = getString(getColumnIndexOrThrow(PostColumns.COLUMN_URL)),
//                likes = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
//                shares = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARES)),
//                viewes = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_VIEWES)),
//                iLiked = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_ILIKES))!=0
//            )
//        }
//    }
//}
//
//
