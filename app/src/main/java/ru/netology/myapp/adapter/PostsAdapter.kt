package ru.netology.myapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myapp.R
import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.databinding.CardPostBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.numberToString
import java.util.Collections.emptyList

typealias OnLikeListener =(post: Post) -> Unit
typealias OnShareListener =(post: Post) -> Unit


class PostsAdapter(private val onLikeListener: OnLikeListener,
                   private val onShareListener: OnShareListener
                   ): ListAdapter<Post, PostsAdapter.PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostViewHolder(binding, onLikeListener, onShareListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class PostViewHolder(val binding: CardPostBinding, private val onLikeListener: OnLikeListener, private val onShareListener: OnShareListener): RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post) {
            binding.apply {
                content.text = post.content
                autor.text = post.autor
                published.text = post.published
                likes.text = numberToString(post.likes)
                share.text = numberToString(post.shares)
                viewed.text = numberToString(post.viewes)
                val imgLike = if (post.iLiked) {
                    R.drawable.ic_baseline_favorite_24
                } else {
                    R.drawable.ic_outline_favorite_border_24
                }
                imagyLikes.setImageResource(imgLike)
                imagyLikes.setOnClickListener {
                    onLikeListener(post)
                }
                imageShare.setOnClickListener {
                    onShareListener(post)
                }
            }
        }
    }
}