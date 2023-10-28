package ru.netology.myapp

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import ru.netology.myapp.OnePostFragment.Companion.textArg
import ru.netology.myapp.databinding.FragmentOneImageBinding
import ru.netology.myapp.databinding.FragmentOneVideoBinding
import ru.netology.myapp.viewmodel.PostViewModel

class OneVideoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentOneVideoBinding.inflate(
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
                println("video1  " + post)
                println("video2   " + post?.attachment?.url != null)
                val isPlaying = true
                binding.apply {

                    playOrPause.setOnClickListener {
                        videoView.apply {
                            setMediaController(MediaController(context))
                            setVideoURI(
                                Uri.parse((post?.attachment?.url))
                            )

                            setOnPreparedListener {
                                start()
                            }
                            setOnCompletionListener {
                                stopPlayback()
                            }
                        }
                    }
                }
            }
        }

        return binding.root
    }
}

//java.lang.NullPointerException: uriString


//Post(id=2259, authorId=545, author=Роман, authorAvatar=null, authorJob=Job, content=Hi!!!, published=2023-10-13T13:24:17.568151Z, coords=null, link=null, likeOwnerIds=null, mentionIds=null, mentionMe=false, likedByMe=false, attachment=null, ownedByMe=false, users=null)