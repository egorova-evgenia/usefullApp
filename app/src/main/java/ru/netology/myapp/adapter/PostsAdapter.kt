package ru.netology.myapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.netology.myapp.R
//import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.databinding.CardPostBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.numberToString
import java.util.Collections.emptyList

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

        fun attach(post: Post){
            val BASE_URL ="http://10.0.2.2:9999"
            val url="$BASE_URL/media/${post.attachment?.url}"
            Glide.with(binding.attachImage)
                .load(url)
                .error(R.drawable.baseline_font_download_off_24)
                .timeout(10_000)
                .into(binding.attachImage)
        }

        fun setAvatar(post: Post){
            val BASE_URL ="http://10.0.2.2:9999"
            val url="$BASE_URL/avatars/${post.authorAvatar}"
            Glide.with(binding.avatar)
                .load(url)
                .placeholder(R.drawable.baseline_downloading_100)
                .error(R.drawable.baseline_font_download_off_24)
                .circleCrop()
                .timeout(10_000)
                .into(binding.avatar)
        }
        fun bind(post: Post) {
            if(post.attachment!=null) {attach(post)}
            setAvatar(post)
            binding.apply {
                content.text = post.content
                autor.text = post.author
                published.text = post.published.toString()

                imageViewed.text = 1.toString()

                buttonLikes.isChecked=post.likedByMe
                buttonLikes.text=numberToString(post.likes)
                buttonShare.text= 2.toString()

                buttonLikes.setOnClickListener {
                    listener.onLike(post)
                }

                buttonShare.setOnClickListener {
                    listener.onShare(post)
                }

                content.setOnClickListener {
                    listener.onShowOnePost(post)
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

                attachImage.setOnClickListener {
                    listener.onShowOneImage(post)
                }

            }

        }

    }
}