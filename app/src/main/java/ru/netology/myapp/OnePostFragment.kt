package ru.netology.myapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.netology.myapp.databinding.CardPostBinding
import ru.netology.myapp.viewmodel.PostViewModel

class OnePostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CardPostBinding.inflate(
            inflater,
            container,
            false
        )
        val viewModel: PostViewModel by activityViewModels()
//        val authViewModel: AuthViewModel by activityViewModels()

        val postId = arguments?.textArg?.toInt()
        if (postId != null) {
            val dataPost = viewModel.getPostById(postId)



            dataPost.observe(viewLifecycleOwner) {
                val post = dataPost.value

                println(post)
//                binding.buttonLikes.text = numberToString(post.likes)
                binding.apply {
//
                    content.text = post!!.content
                    autor.text = post.author
                    published.text = post.published.toString()
//
//                    imageViewed.text = numberToString(0)
//
//                    buttonLikes.isChecked = post.likedByMe
//                    buttonLikes.text = numberToString(post.likes)
//                    buttonShare.text = numberToString(2)
//
//                    buttonLikes.setOnClickListener {
//                        viewModel.run {
//                            if (post.likedByMe) {
//                                viewModel.disLikeById(post.id)
//                            } else {
//                                viewModel.likeById(post.id)
//                            }
//                        }
//                    }
//
//                    buttonShare.setOnClickListener {
//                        val intent = Intent()
//                            .setAction(Intent.ACTION_SEND)
//                            .setType("text/plain")
//                        val createChooser = Intent.createChooser(intent, "Choose app")
//                        startActivity(createChooser)
//                        viewModel.shareById(post.id)
//                    }
//
//                    menu.setOnClickListener {
//                        PopupMenu(it.context, it).apply {
//                            inflate(R.menu.post_menu)
//                            setOnMenuItemClickListener { menuItem ->
//                                when (menuItem.itemId) {
//                                    R.id.remove -> {
//                                        viewModel.removeById(post.id)
//                                        findNavController().navigateUp()
//
//                                        //                                        R.id.action_onePostFragment_to_FeedFragment)
//                                        return@setOnMenuItemClickListener true
//                                    }
//
//                                    R.id.edit -> {
//                                        findNavController().navigate(
//                                            R.id.action_onePostFragment_to_editFragment,
//                                            Bundle().apply
//                                            { textArg = post.content }
//                                        )
//                                        if (post != null) {
//                                            viewModel.edit(post)
//                                        }
//                                        return@setOnMenuItemClickListener true
//                                    }
//                                }
//                                false
//                            }
//
//                            show()
//                        }
//                    }
                }
            }
        }

        return binding.root
    }
    companion object {
        var Bundle.textArg: String? by StringArg
    }
}
