package ru.netology.myapp

/*import android.icu.number.NumberFormatter.with*/
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.myapp.adapter.PostsAdapter
import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.viewmodel.PostViewModel

/*@Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")*/
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel>()
        val adapter = PostsAdapter ({
            viewModel.likeById(it.id)},
            {viewModel.shareById(it.id)}
        )
        binding.list.adapter=adapter

        val observe = viewModel.data.observe(this) { posts ->
            posts.map { post ->
                adapter.list = posts
//                println("adapter")
            }

        }
    }
}








