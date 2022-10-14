package ru.netology.myapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myapp.R
import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.databinding.CardPostBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.numberToString
import java.util.Collections.emptyList

//typealias OnLikeListener =(post: Post) -> Unit
//typealias OnShareListener =(post: Post) -> Unit
//typealias OnRemoveListener =(post: Post) -> Unit

interface PostEventListener{
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onCancelEdit(post: Post)
}

class PostsAdapter(private val listener: PostEventListener
                   ): ListAdapter<Post, PostsAdapter.PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
    class PostViewHolder(val binding: CardPostBinding,
                         val listener: PostEventListener
                         ): RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post) {
            binding.apply {
                content.text = post.content
                autor.text = post.autor
                published.text = post.published

                imageViewed.text = numberToString(post.viewes)

                buttonLikes.isChecked=post.iLiked
                buttonLikes.text=numberToString(post.likes)
                buttonShare.text=numberToString(post.shares)

                buttonLikes.setOnClickListener {
                    listener.onLike(post)
                }

                buttonShare.setOnClickListener {
                    listener.onShare(post)
                }

                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.post_menu)
                        setOnMenuItemClickListener {menuItem ->
                            when (menuItem.itemId){
                                R.id.remove -> {listener.onRemove(post)
                                return@setOnMenuItemClickListener true}
                                R.id.edit -> {listener.onEdit(post)
                                    return@setOnMenuItemClickListener true}
                            }
                            false
                        }

                        show()
                    }
                }
            }
        }
    }
}