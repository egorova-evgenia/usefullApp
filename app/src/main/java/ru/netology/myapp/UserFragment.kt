package ru.netology.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import ru.netology.myapp.OnePostFragment.Companion.textArg
import ru.netology.myapp.databinding.CardUserBinding
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
            val url = "${BuildConfig.BASE_URL}/avatars/${user.avatar}"
            println(url)
            Glide.with(binding.avatar)
                .load(url)
                .error(R.drawable.baseline_font_download_off_24)
                .timeout(10_000)
                .into(binding.avatar)
        }

        val viewModel: PostViewModel by activityViewModels()
        val userViewModel: UserViewModel by activityViewModels()
        val userId = arguments?.textArg?.toInt()



        if (userId != null) {
            val dataUser = userViewModel.getUserById(userId)
            dataUser.observe(viewLifecycleOwner) {

                val user = dataUser.value
                if (user?.avatar != null) {
                    setAvatar(user)
                    binding.apply {
                        name.text = user!!.name

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