package ru.netology.myapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myapp.R
import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.databinding.CardPostBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.numberToString
import java.util.Collections.emptyList

typealias OnLikeListener =(post: Post) -> Unit

class PostsAdapter(private val onLikeListener: OnLikeListener): RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {
    var list = emptyList<Post>()
    set(value){
        field=value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostViewHolder(binding, onLikeListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = list[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = list.size

    class PostViewHolder(private val binding: CardPostBinding, private val onLikeListener: OnLikeListener): RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post) {
            binding.apply {
                binding.content.text = post.content
                binding.autor.text = post.autor
                binding.published.text = post.published
                binding.likes.text = numberToString(post.likes)
                binding.share.text = numberToString(post.shares)
                binding.viewed.text = numberToString(post.viewes)
                val imgLike = if (post.iLiked) {
                    R.drawable.ic_baseline_favorite_24
                } else {
                    R.drawable.ic_outline_favorite_border_24
                }
                binding.imagyLikes.setImageResource(imgLike)

                binding.imagyLikes.setOnClickListener {
                    onLikeListener(post)
                }
            }
        }
    }
}