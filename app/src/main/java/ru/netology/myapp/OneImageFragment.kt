package ru.netology.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import ru.netology.myapp.BuildConfig.BASE_URL
import ru.netology.myapp.OnePostFragment.Companion.textArg
import ru.netology.myapp.databinding.FragmentOneImageBinding
import ru.netology.myapp.viewmodel.PostViewModel

class OneImageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentOneImageBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel: PostViewModel by activityViewModels()
        val postId = arguments?.textArg?.toInt()

        if (postId != null) {
            val dataPost = viewModel.getPostById(postId)
            dataPost.observe(viewLifecycleOwner) {
                val post = dataPost.value
                val url = "${post?.attachment?.url}"
                println("postdata1 url   " + url)
                Glide.with(binding.oneImage)
                    .load(url)
                    .error(R.drawable.baseline_font_download_off_24)
                    .timeout(10_000)
                    .into(binding.oneImage)
            }
        }
        return binding.root
    }
}