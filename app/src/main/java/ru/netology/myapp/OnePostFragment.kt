package ru.netology.myapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.myapp.FeedFragment.Companion.textArg
import ru.netology.myapp.OnePostFragment.Companion.textArg
import ru.netology.myapp.adapter.PostEventListener
import ru.netology.myapp.adapter.PostViewHolder
import ru.netology.myapp.adapter.PostsAdapter
import ru.netology.myapp.databinding.CardPostBinding
import ru.netology.myapp.databinding.FragmentFeedBinding
import ru.netology.myapp.databinding.FragmentOnePostBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.viewmodel.PostViewModel

class OnePostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CardPostBinding.inflate(
            inflater,
            container,
            false
        )
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val postId = arguments?.textArg?.toInt()
        if (postId!=null) {
            val post = viewModel.findPost(postId)

        binding.apply {
            content.text = post.content
            autor.text = post.autor
            published.text = post.published

            imageViewed.text = numberToString(post.viewes)

            buttonLikes.isChecked = post.iLiked
            buttonLikes.text = numberToString(post.likes)
            buttonShare.text = numberToString(post.shares)

            buttonLikes.setOnClickListener {
                viewModel.likeById(post.id)
            }

            buttonShare.setOnClickListener {
                val intent = Intent()
                    .setAction(Intent.ACTION_SEND)
                    .setType("text/plain")
                val createChooser = Intent.createChooser(intent, "Choose app")
                startActivity(createChooser)
                viewModel.shareById(post.id)
            }

                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.post_menu)
                        setOnMenuItemClickListener {menuItem ->
                            when (menuItem.itemId){
                                R.id.remove -> {
                                    viewModel.removeById(post.id)
                                    findNavController().navigateUp()

//                                        R.id.action_onePostFragment_to_FeedFragment)
                                    return@setOnMenuItemClickListener true}
                                R.id.edit -> {
                                    findNavController().navigate(
                                        R.id.action_onePostFragment_to_editFragment,
                                        Bundle().apply
                                        { textArg = post.content }
                                    )
                                    viewModel.edit(post)
                                    return@setOnMenuItemClickListener true}
                            }
                            false
                        }

                        show()
                    }
                }
            }
        }


        val observe = viewModel.data.observe(viewLifecycleOwner) { posts ->

            val post = posts.find { it.id == postId } ?:
            return@observe
            binding.buttonLikes.text = numberToString(post.likes)
            binding.buttonShare.text = numberToString(post.shares)


        }
        return binding.root
    }
    companion object {
        var Bundle.textArg: String? by StringArg
    }
}


//val adapterO = PostsAdapter(
//
//    object : PostEventListener {
//        override fun onLike(post: Post) {
//            viewModel.likeById(post.id)
//        }
//
//        override fun onShare(post: Post) {
//            val intent = Intent()
//                .setAction(Intent.ACTION_SEND)
//                .setType("text/plain")
//            val createChooser = Intent.createChooser(intent, "Choose app")
//            startActivity(createChooser)
//            viewModel.shareById(post.id)
//        }
//
//        override fun onRemove(post: Post) {
//            viewModel.removeById(post.id)
//            findNavController().navigateUp()
//        }
//
//        override fun onEdit(post: Post) {
//
//            findNavController().navigate(
//                R.id.action_onePostFragment_to_editFragment,
//                Bundle().apply
//                { textArg = post.content }
//            )
//            viewModel.edit(post)
//        }
//
//        override fun onCancelEdit(post: Post) {
//            viewModel.edit(post)
//        }
//
//        override fun onShowOnePost(post: Post) {
//
//        }
//    }
//)
//
//binding.list.adapter = adapterO
//
//val observe = viewModel.data.observe(viewLifecycleOwner) { posts ->
//    posts.map { post ->
//        adapterO.submitList(posts)
//    }
//}
