//package ru.netology.myapp
//
///*import android.icu.number.NumberFormatter.with*/
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import androidx.activity.result.launch
//import androidx.activity.viewModels
//import ru.netology.myapp.adapter.PostEventListener
//import ru.netology.myapp.adapter.PostsAdapter
//import ru.netology.myapp.databinding.ActivityMainBinding
//import ru.netology.myapp.dto.Post
//import ru.netology.myapp.repository.utils.AndroidUtils
//import ru.netology.myapp.viewmodel.PostViewModel
//import ru.netology.myapp.viewmodel.newPostId
//
///*@Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")*/
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val viewModel by viewModels<PostViewModel>()
//        val newPostLauncher = registerForActivityResult(NewPostActivityContract()){text->
//            text ?:return@registerForActivityResult
//            viewModel.editContent(text)
//            viewModel.save()
//        }
//
//        val editPostLauncher = registerForActivityResult(EditModeActivityContract()){text->
//            text ?:return@registerForActivityResult //
//            viewModel.editContent(text)
//
//        }
//
//        val sharePostLauncher = registerForActivityResult(intentHandlerActivityContract()){text->
//            text ?:return@registerForActivityResult //
//        }
//
//        val adapter = PostsAdapter (
//            object : PostEventListener{
//                override fun onLike(post: Post) {
//                    viewModel.likeById(post.id)
//                }
//
//                override fun onShare(post: Post) {
//                    sharePostLauncher.launch()
//                    viewModel.shareById(post.id)
//
//                }
//
//                override fun onRemove(post: Post) {
//                    viewModel.removeById(post.id)
//                }
//
//                override fun onEdit(post: Post) {
//
//
//                    viewModel.edit(post)
//                }
//
//                override fun onCancelEdit(post: Post) {
//                    viewModel.edit(post)
//                }
//
//            }
//        )
//
//        viewModel.edited.observe(this) {edited ->
//
//            if (edited.id==newPostId) {
//                return@observe
//            }
//            editPostLauncher.launch(edited.content)// Просит стринг?post.content
////            binding.editTextGroup.visibility=View.VISIBLE
////            binding.content.requestFocus()
//        }
//
////        binding.cancel.setOnClickListener {
////            viewModel.cancelEdit()
////            binding.content.setText("")
////            binding.content.clearFocus()
////            AndroidUtils.hideKeyboard(binding.content)
////            binding.editTextGroup.visibility=View.GONE
////        }
////
////        binding.save.setOnClickListener{
////            if (binding.content.text.isNullOrBlank()) {
////                Toast.makeText( it.context,getString(R.string.empty_post), Toast.LENGTH_SHORT)
////                    .show()
////                return@setOnClickListener
////            }
////            val text = binding.content.text?.toString().orEmpty()
////            viewModel.editContent(text)
////            viewModel.save()
////            binding.content.clearFocus()
////            AndroidUtils.hideKeyboard(binding.content)
////            binding.editTextGroup.visibility=View.GONE
////            binding.content.setText("")
////        }
//
//        binding.list.adapter=adapter
//        val observe = viewModel.data.observe(this) { posts ->
//            posts.map { post ->
//                adapter.submitList(posts)
//            }
//        }
//
//        binding.plus.setOnClickListener(){
////            val isPostNew = true
//            newPostLauncher.launch()
//        }
//    }
//}
//
