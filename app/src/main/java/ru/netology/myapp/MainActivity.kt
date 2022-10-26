package ru.netology.myapp

/*import android.icu.number.NumberFormatter.with*/
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.myapp.adapter.PostEventListener
import ru.netology.myapp.adapter.PostsAdapter
import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.viewmodel.PostViewModel
import ru.netology.myapp.viewmodel.newPostId

/*@Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")*/
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel>()

        val newPostLauncher = registerForActivityResult(NewPostActivityContract()){text->
            text ?:return@registerForActivityResult
            viewModel.editContent(text.toString())
            viewModel.save()
        }

        val editPostLauncher = registerForActivityResult(EditActivityContract()){text->
            text ?:return@registerForActivityResult
            viewModel.editContent(text.toString())
            viewModel.save()
        }

        val adapter = PostsAdapter (
            object : PostEventListener{
                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShare(post: Post) {

                    viewModel.shareById(post.id)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onEdit(post: Post) {
//                    val intent = Intent().apply {
//                        intent.putExtra(Intent.EXTRA_TEXT, post.content)
//                    }
//                    startActivity(intent)
//                    val putIntent = Intent()
//                        action = Intent().ACTION_SEND
//                        putExtra()
////                        val text = getStringExtra()
//                    }
                    editPostLauncher.launch(post.content.toString())
                    viewModel.edit(post)
                }

                override fun onCancelEdit(post: Post) {
                    viewModel.edit(post)
                }

            }
        )

        viewModel.edited.observe(this) {edited ->

            if (edited.id==newPostId) {
                return@observe
            }
//            binding.content.setText(edited.content)
//            binding.editTextGroup.visibility=View.VISIBLE
//            binding.content.requestFocus()
        }
//
//        binding.cancel.setOnClickListener {
//            viewModel.cancelEdit()
//            binding.content.setText("")
//            binding.content.clearFocus()
//            AndroidUtils.hideKeyboard(binding.content)
//            binding.editTextGroup.visibility=View.GONE
//        }

//        binding.save.setOnClickListener{
//            if (binding.content.text.isNullOrBlank()) {
//                Toast.makeText( it.context,getString(R.string.empty_post), Toast.LENGTH_SHORT)
//                    .show()
//                return@setOnClickListener
//            }
//            val text = binding.content.text?.toString().orEmpty()
//            viewModel.editContent(text)
//            viewModel.save()
//            binding.content.clearFocus()
//            AndroidUtils.hideKeyboard(binding.content)
//            binding.editTextGroup.visibility=View.GONE
//            binding.content.setText("")
//        }

        binding.list.adapter=adapter
        val observe = viewModel.data.observe(this) { posts ->
            posts.map { post ->
                adapter.submitList(posts)
            }
        }

        binding.plus.setOnClickListener{
            newPostLauncher.launch()
//             registerForActivityResult(NewPostActivityContract()){text->
//            text ?:return@registerForActivityResult
//                 viewModel.editContent(text)
//                 viewModel.save()
//        }.launch()
        }
    }
}