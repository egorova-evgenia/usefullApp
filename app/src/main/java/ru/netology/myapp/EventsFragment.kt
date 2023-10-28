package ru.netology.myapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.myapp.adapter.PostEventListener
import ru.netology.myapp.adapter.PostLoadingStateAdapter
import ru.netology.myapp.adapter.PostsAdapter
import ru.netology.myapp.auth.AppAuth
import ru.netology.myapp.databinding.FragmentEventBinding
import ru.netology.myapp.databinding.FragmentFeedBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.viewmodel.AuthViewModel
import ru.netology.myapp.viewmodel.EventViewModel
import javax.inject.Inject

@AndroidEntryPoint
class EventsFragment : Fragment(

) {

    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventBinding.inflate(
            inflater,
            container,
            false
        )

        val authViewModel: AuthViewModel by activityViewModels()
        val eventViewModel: EventViewModel by activityViewModels()

        val adapter = PostsAdapter(

            object : PostEventListener {
                override fun onLike(post: Post) {
//                    if (post.likedByMe) {
//                        println(post.likedByMe)
//                        eventViewModel.disLikeById(post.id)
//                    } else {
//                        println(post.likedByMe)
//                        eventViewModel.likeById(post.id)
//                    }
                }

                override fun onShare(post: Post) {
//                    val intent = Intent()
//                        .setAction(Intent.ACTION_SEND)
//                        .setType("text/plain")
//                    val createChooser = Intent.createChooser(intent, "Choose app")
//                    startActivity(createChooser)
//                    viewModel.shareById(post.id)
                }

                override fun onRemove(post: Post) {
//                    viewModel.removeById(post.id)
                }

                override fun onEdit(post: Post) {
//                    findNavController().navigate(
//                        R.id.action_feedFragment_to_editFragment,
//                        Bundle().apply
//                        { textArg = post.content }
//                    )
//                    viewModel.edit(post)
                }

                override fun onCancelEdit(post: Post) {
//                    viewModel.edit(post)
                }

                override fun onShowOnePost(post: Post) {
//                    findNavController().navigate(
//                        R.id.action_feedFragment_to_onePostFragment,
//                        Bundle().apply
//                        { textArg = post.id.toString() }
//                    )
                }

                override fun onShowOneImage(post: Post) {
//                    findNavController().navigate(
//                        R.id.action_feedFragment_to_imageFragment,
//                        Bundle().apply
//                        { textArg = post.id.toString() }
//                    )
                }

                override fun onShowVideoFragment(post: Post) {
                    TODO("Not yet implemented")
                }

                override fun onControlAudio(post: Post) {
                    TODO("Not yet implemented")
                }

                override fun onShowUser(post: Post) {
//                    findNavController().navigate(
//                        R.id.action_feedFragment_to_userFragment,
//                        Bundle().apply
//                        { textArg = post.authorId.toString() }
//                    )
                }

            }
        )

        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PostLoadingStateAdapter { adapter.retry() },
            footer = PostLoadingStateAdapter { adapter.retry() }
        )
        binding.plus.setOnClickListener {
            if (appAuth.authStateFlow.value.id != 0) {
                findNavController().navigate(R.id.action_feedFragment_to_editFragment)
            } else {
                findNavController().navigate(R.id.action_feedFragment_to_AuthDialog)
            }

        }

        binding.toFeed.setOnClickListener {
            findNavController().navigate(R.id.action_EventsFragment_to_feedFragment)
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        })

        lifecycleScope.launch {
            eventViewModel.eventsData.collectLatest {
                adapter.submitData(it)
            }
        }

        eventViewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
//            binding.errorGroup.isVisible = state.error
            binding.swiprefresh.isRefreshing = state.loading
            if (state.error) {
                Snackbar.make(binding.root, "ошибка", Snackbar.LENGTH_LONG)
                    .setAction("Retry") { eventViewModel.loadEvents() }
                    .show()
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                binding.swiprefresh.isRefreshing = it.refresh is LoadState.Loading
            }
        }

        binding.swiprefresh.setOnRefreshListener {
            adapter.refresh()
        }

        var menuProvider: MenuProvider? = null

        authViewModel.state.observe(viewLifecycleOwner) { _ ->
            menuProvider?.let {
                requireActivity().removeMenuProvider(it)
            }
            requireActivity().addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.main_menu, menu)
                    menu.setGroupVisible(R.id.authorized, authViewModel.authorized)
                    menu.setGroupVisible(R.id.unauthorized, !authViewModel.authorized)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.signout -> {
                            appAuth.clear()
                            true
                        }

                        R.id.signin -> {
                            findNavController().navigate(R.id.action_feedFragment_to_signInFragment)
                            true
                        }

                        R.id.signup -> {
                            findNavController().navigate(R.id.action_feedFragment_to_regFragment)
                            true
                        }

                        else -> false
                    }
                }
            }.apply { menuProvider = this }, viewLifecycleOwner)
        }
        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }

}
