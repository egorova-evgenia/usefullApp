package ru.netology.myapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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
                    if (post.likedByMe)
                    {
                        println(post.likedByMe)
                        viewModel.disLikeById(post.id)
                    }
                    else {
                        println(post.likedByMe)
                        viewModel.likeById(post.id)
                    }
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
            }
        )

        binding.list.adapter=adapter
        binding.plus.setOnClickListener{
            findNavController().navigate(R.id.action_feedFragment_to_editFragment)
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart==0) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        })

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it.posts)
            binding.emptyText.isVisible = it.empty
        }

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
//            binding.errorGroup.isVisible = state.error
            binding.swiprefresh.isRefreshing = state.loading
            if (state.error) {
                Snackbar.make(binding.root, "ошибка", Snackbar.LENGTH_LONG)
                    .setAction("Retry") {viewModel.loadPosts()}
                    .show()
            }
        }

        viewModel.newerCount.observe(viewLifecycleOwner){
            println("Newer cout: $it")
            if (it != 0) {
                binding.showNewPost.visibility = View.VISIBLE
//                Snackbar.make(binding.root, "Новые", Snackbar.LENGTH_LONG)
//                    .setAction(android.R.string.ok) {
//                        viewModel.loadPosts()
//                        viewModel.changeHidden()
//                    }.show()
            }
        }

        binding.showNewPost.setOnClickListener {
            viewModel.changeHidden()
            binding.showNewPost.visibility = View.GONE
        }

//        binding.retryButton.setOnClickListener{
//            viewModel.loadPosts()
//        }

        binding.swiprefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }

}