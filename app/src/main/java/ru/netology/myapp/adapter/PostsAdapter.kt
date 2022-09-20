package ru.netology.myapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myapp.R
import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.databinding.CardPostBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.numberToString
import java.util.Collections.emptyList

typealias OnLikeListener =(post: Post) -> Unit
typealias OnShareListener =(post: Post) -> Unit
typealias OnRemoveListener =(post: Post) -> Unit



class PostsAdapter(private val onLikeListener: OnLikeListener,
                   private val onShareListener: OnShareListener,
                   private val onRemoveListener: OnRemoveListener
                   ): RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {
    var list = emptyList<Post>()
    set(value){
        field=value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostViewHolder(binding=binding,
            onLikeListener=onLikeListener,
            onShareListener=onShareListener,
            onRemoveListener=onRemoveListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = list[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = list.size
//    , val onLikeListener: OnLikeListener
    class PostViewHolder(val binding: CardPostBinding,
                         private val onLikeListener: OnLikeListener,
                         private val onShareListener: OnShareListener,
                         private val onRemoveListener: OnRemoveListener
                         ): RecyclerView.ViewHolder(binding.root){
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

                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.post_menu)
                        setOnMenuItemClickListener {menuItem ->
                            when (menuItem.itemId){
                                R.id.remove -> {onRemoveListener(post)
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