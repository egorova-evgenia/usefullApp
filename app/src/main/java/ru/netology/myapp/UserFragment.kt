package ru.netology.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import ru.netology.myapp.OnePostFragment.Companion.textArg
import ru.netology.myapp.adapter.JobEventListener
import ru.netology.myapp.adapter.JobsAdapter
import ru.netology.myapp.adapter.PostsAdapter
import ru.netology.myapp.databinding.CardUserBinding
import ru.netology.myapp.dto.Job
import ru.netology.myapp.dto.Post
import ru.netology.myapp.dto.User
import ru.netology.myapp.viewmodel.PostViewModel
import ru.netology.myapp.viewmodel.UserViewModel

class UserFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CardUserBinding.inflate(
            inflater,
            container,
            false
        )

        fun setAvatar(user: User) {
            val url = "${user.avatar}"
            println(url)
            Glide.with(binding.avatar)
                .load(url)
                .error(R.drawable.baseline_font_download_off_24)
                .timeout(10_000)
                .into(binding.avatar)
        }

        val userViewModel: UserViewModel by activityViewModels()
        val userId = arguments?.textArg?.toInt()
        println("useruserId    " + userId)


        if (userId != null) {
            val dataUser = userViewModel.getUserById(userId)
            userViewModel.getJobsForUser(userId)

            var toShow: Boolean = false

            binding.toShow.setOnClickListener {
                toShow = !toShow
                binding.jobList.isVisible = toShow
                if (toShow == false) binding.toShow.setIconResource(R.drawable.baseline_keyboard_double_arrow_down_24)
                else binding.toShow.setIconResource(R.drawable.baseline_keyboard_double_arrow_up_24)
            }

            val adapter = JobsAdapter(object : JobEventListener {
                override fun onRemove(job: Job) {
                    TODO("Not yet implemented")
                }

                override fun onEdit(job: Job) {
                    TODO("Not yet implemented")
                }

                override fun onCancelEdit(job: Job) {
                    TODO("Not yet implemented")
                }
            })

            binding.jobList.adapter = adapter

            binding.jobList.addItemDecoration(
                DividerItemDecoration(binding.jobList.context, DividerItemDecoration.VERTICAL)
            )

            userViewModel.userJobs.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            userViewModel.user.observe(viewLifecycleOwner) {

                val user = userViewModel.user.value
                println("userNew   " + user)
                if (user != null) {
                    if (user.avatar != null) {
                        setAvatar(user)
                    }
                    binding.apply {
                        name.text = user.login
                    }
                }


            }
        }

        return binding.root
    }


    companion object {
        var Bundle.textArg: String? by StringArg
    }

}