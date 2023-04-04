package ru.netology.myapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.myapp.adapter.PostEventListener
import ru.netology.myapp.adapter.PostsAdapter
import ru.netology.myapp.databinding.FragmentFeedBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.viewmodel.PostViewModel

/*@Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")*/
class FeedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val adapter = PostsAdapter (

            object : PostEventListener{
                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShare(post: Post) {
                    val intent = Intent()
                        .setAction(Intent.ACTION_SEND)
                        .setType("text/plain")
                    val createChooser = Intent.createChooser(intent, "Choose app")
                    startActivity(createChooser)
                    viewModel.shareById(post.id)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onEdit(post: Post) {

                    findNavController().navigate(
                        R.id.action_feedFragment_to_editFragment,
                    Bundle().apply
                     { textArg = post.content }
                    )
                    viewModel.edit(post)
                }

                override fun onCancelEdit(post: Post) {
                    viewModel.edit(post)
                }

                override fun onShowOnePost(post: Post){
                    findNavController().navigate(
                        R.id.action_feedFragment_to_onePostFragment,
                        Bundle().apply
                        { textArg = post.id.toString() }
                    )
                }

                override fun refresh() {
                    viewModel.refresh()
                }
            }
        )

        binding.list.adapter=adapter

        viewModel.data.observe(viewLifecycleOwner, { state ->
            adapter.submitList(state.posts)
            binding.progress.isVisible = state.loading
            binding.errorGroup.isVisible = state.error
            binding.emptyText.isVisible = state.empty
            binding.swiprefresh.isVisible = state.swiprefresh
        })

        binding.plus.setOnClickListener{
            findNavController().navigate(R.id.action_feedFragment_to_editFragment)
        }

        binding.retryButton.setOnClickListener{
            viewModel.loadPosts()
        }
        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }

}