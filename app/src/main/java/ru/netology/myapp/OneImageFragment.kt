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

        val postId = arguments?.textArg?.toLong()

        if (postId!=null) {
            viewModel.data.observe(viewLifecycleOwner) {
                val post = it.posts.find { it.id == postId } ?: return@observe
//                post.attachment?.
                val BASE_URL ="http://10.0.2.2:9999"
                val url="$BASE_URL/media/${post.attachment?.url}"
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