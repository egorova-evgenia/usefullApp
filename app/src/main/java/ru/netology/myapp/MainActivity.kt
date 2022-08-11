package ru.netology.myapp

/*import android.icu.number.NumberFormatter.with*/
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
/*import android.widget.ImageView*/
import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.viewmodel.PostViewModel

/*@Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")*/
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel>()
        viewModel.data.observe(this){ post ->
            binding.content.text = post.content
            binding.autor.text = post.autor
            binding.published.text = post.published
            binding.likes.text = numberToString(post.likes)
            binding.share.text = numberToString(post.shares)
            binding.viewed.text = numberToString(post.viewes)

            val imgLike = if (post.iLiked) {
                    R.drawable.ic_baseline_favorite_24
                } else {
                    R.drawable.ic_outline_favorite_border_24
                }
            binding.imagyLikes.setImageResource(imgLike)
        }
         binding.imagyLikes.setOnClickListener {
            viewModel.like()
        }
        binding.imageShare.setOnClickListener {
            viewModel.share()
        }

    }
}
/*???
private operator fun <T> Lazy<T>.getValue(t: T?, property: KProperty<T?>): T {

}*/







