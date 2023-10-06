package ru.netology.myapp.adapter

import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.myapp.BuildConfig.BASE_URL
import ru.netology.myapp.R
import ru.netology.myapp.databinding.CardEventBinding
import ru.netology.myapp.databinding.CardPostBinding
import ru.netology.myapp.dto.Event
import ru.netology.myapp.dto.Post

class EventViewHolder(
    val binding: CardEventBinding,
    val listener: PostEventListener
) : RecyclerView.ViewHolder(binding.root) {
    fun attach(item: Event) {
        val url = "$BASE_URL/media/${item.attachment?.url}"
        Glide.with(binding.attachImage)
            .load(url)
//                .placeholder(R.drawable.baseline_downloading_100)
            .error(R.drawable.baseline_font_download_off_24)
            .timeout(10_000)
            .into(binding.attachImage)
    }

    fun setAvatar(item: Event) {
        val url = "$BASE_URL/avatars/${item.authorAvatar}"
        Glide.with(binding.avatar)
            .load(url)
            .error(R.drawable.baseline_font_download_off_24)
            .timeout(10_000)
            .into(binding.avatar)
    }

    fun bind(item: Event) {
        if (item.attachment != null) {
            attach(item)
        }
        setAvatar(item)
        binding.apply {
            content.text = item.content
            autor.text = item.author
            published.text = item.published.toString()


            buttonLikes.isChecked = item.likedByMe
//          todo  buttonLikes.text = post.likes.toString()
            buttonLikes.text = 0.toString()
            buttonShare.text = 2.toString()

            buttonLikes.setOnClickListener {
//                listener.onLike(item)
            }

            buttonShare.setOnClickListener {
//                listener.onShare(item)
            }

            content.setOnClickListener {
//                listener.onShowOnePost(item)
            }

            menu.isVisible = item.ownedByMe
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
//                                listener.onRemove(item)
                                return@setOnMenuItemClickListener true
                            }

                            R.id.edit -> {
//                                listener.onEdit(item)
                                return@setOnMenuItemClickListener true
                            }
                        }
                        false
                    }

                    show()
                }
            }

            attachImage.setOnClickListener {
//                listener.onShowOneImage(item)
            }
        }
    }
}