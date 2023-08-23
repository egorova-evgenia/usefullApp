package ru.netology.myapp.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.myapp.BuildConfig.BASE_URL
import ru.netology.myapp.R
import ru.netology.myapp.databinding.CardAdBinding
import ru.netology.myapp.dto.Ad

class AdViewHolder(
    private val binding: CardAdBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(ad: Ad) {
        val idAd = ad.id
//        Glide.with(binding.adImage).load("$BASE_URL/media/${ad.image}")
//        binding.adImage.load("$BASE_URL/media/${ad.image}")
//        val url = "$BASE_URL/media/${ad.image}"
//        Glide.with(binding.adImage)
//            .load(url)
//            .error(R.drawable.baseline_font_download_off_24)
//            .timeout(10_000)
//            .into(binding.adImage)
    }
}