package ru.netology.myapp.adapter

import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.myapp.BuildConfig.BASE_URL
import ru.netology.myapp.R
import ru.netology.myapp.databinding.CardPostBinding
import ru.netology.myapp.dto.AttachmentType
import ru.netology.myapp.dto.Post
//import ru.netology.myapp.numberToString

class PostViewHolder(
    val binding: CardPostBinding,
    val listener: PostEventListener
) : RecyclerView.ViewHolder(binding.root) {
    fun attach(post: Post) {
        println("att2+ " + post.attachment?.url)
        val url = "${post.attachment?.url}"

        val type = post.attachment?.type
        println(type.toString())
        when (type) {
            AttachmentType.IMAGE -> {
                println("here image  " + url)
                Glide.with(binding.attachImage)
                    .load(url)
//                    .placeholder(R.drawable.baseline_downloading_100)
                    .error(R.drawable.baseline_font_download_off_24)
                    .timeout(10_000)
                    .into(binding.attachImage)
                binding.videoGroup.isVisible = false
                binding.audioGroup.isVisible = false
                binding.attachImage.isVisible = true
                binding.attachmentContainer.isVisible = true

            }

            AttachmentType.VIDEO -> {
                binding.videoGroup.isVisible = true
                binding.audioGroup.isVisible = false
                binding.attachImage.isVisible = false
                binding.attachmentContainer.isVisible = true
            }

            AttachmentType.AUDIO -> {
                binding.audioGroup.isVisible = true
                binding.videoGroup.isVisible = false
                binding.attachImage.isVisible = false
                binding.attachmentContainer.isVisible = true
            }

            else -> {
                binding.attachmentContainer.isVisible = false
            }
        }
    }

    fun setAvatar(post: Post) {
        val url = "${post.authorAvatar}"
        Glide.with(binding.avatar)
            .load(url)
            .error(R.drawable.baseline_font_download_off_24)
            .timeout(10_000)
            .into(binding.avatar)
    }

    fun bind(post: Post) {
        println("att5+ " + post.attachment?.url)
        if (post.attachment != null) {
            println("att+  " + post.attachment.url)
            attach(post)
        }
        if (post.authorAvatar != null) {
            setAvatar(post)
        } else {
            binding.avatar.setImageResource(R.drawable.baseline_question_mark_24)
        }

        binding.apply {
            content.text = post.content
            autor.text = post.author
            published.text = post.published.toString()


            buttonLikes.isChecked = post.likedByMe
//          todo  buttonLikes.text = post.likes.toString()
            buttonLikes.text = 0.toString()
            buttonShare.text = 2.toString()

            buttonLikes.setOnClickListener {
                listener.onLike(post)
            }

            buttonShare.setOnClickListener {
                listener.onShare(post)
            }

            content.setOnClickListener {
                listener.onShowOnePost(post)
            }

            menu.isVisible = post.ownedByMe
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                listener.onRemove(post)
                                return@setOnMenuItemClickListener true
                            }

                            R.id.edit -> {
                                listener.onEdit(post)
                                return@setOnMenuItemClickListener true
                            }
                        }
                        false
                    }

                    show()
                }
            }

            attachImage.setOnClickListener {
                listener.onShowOneImage(post)
            }

            videoGroup.setOnClickListener {
                listener.onShowVideoFragment(post)
            }

            playOrPause.setOnClickListener {
                listener.onControlAudio(post)
            }

            avatar.setOnClickListener {
                listener.onShowUser(post)
            }
        }
    }
}

