package ru.netology.myapp

/*import android.icu.number.NumberFormatter.with*/
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
/*import android.widget.ImageView*/
import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*
        val post = Post(
                id = 0,
                autor = "Netology. Длииииииииииииииииииииииииииииииииииииинно",
                autorAvatar = "@tools:sample/avatars",
                published = "30 июля в 10.30",
                content = "When a view's measure() method returns, its getMeasuredWidth() and getMeasuredHeight() values must be set, along with those for all of that view's descendants. A view's measured width and measured height values must respect the constraints imposed by the view's parents. This guarantees that at the end of the measure pass, all parents accept all of their children's measurements. A parent view may call measure() more than once on its children. For example, the parent may measure each child once with unspecified dimensions to find out how big they want to be, then call measure() on them again with actual numbers if the sum of all the children's unconstrained sizes is too big or too small.",
                url = "www.url.ru",
                likes = 0,
                shares = 999,
                viewes = 22)*/

        val viewModel: PostViewModel by viewModels()

/*
        binding.content.text = post.content
        binding.autor.text = post.autor
        binding.published.text = post.published
        var likesNow = post.likes
        binding.likes.text = likesNow.toString()
        binding.share.text = post.shares.toString()
        binding.viewed.text = post.viewes.toString()

        binding.imagyLikes.setOnClickListener {
            post.iLiked = !post.iLiked
            binding.likes.text = if (post.iLiked) numberToString(likesNow + 1) else numberToString(likesNow)
            binding.imagyLikes.setImageResource(
                    if (post.iLiked) {
                        R.drawable.ic_baseline_favorite_24
                    } else {
                        R.drawable.ic_outline_favorite_border_24
                    }
            )
        }

        binding.imageShare.setOnClickListener {
            post.shares = post.shares+1
            binding.share.text = numberToString(post.shares)
        }

*/
    }
}