package ru.netology.myapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myapp.BuildConfig
import ru.netology.myapp.R
import ru.netology.myapp.databinding.CardAdBinding
import ru.netology.myapp.databinding.CardEventBinding
import ru.netology.myapp.databinding.CardPostBinding
import ru.netology.myapp.dto.Ad
import ru.netology.myapp.dto.Event
import ru.netology.myapp.dto.FeedItem
import ru.netology.myapp.dto.Post
import androidx.recyclerview.widget.ListAdapter

class PostListAdapter(
    private val listener: PostEventListener
) : ListAdapter<FeedItem, RecyclerView.ViewHolder>(PostDiffCallback()) {

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is Ad -> R.layout.card_ad
            is Post -> R.layout.card_post
            is Event -> R.layout.card_event
            null -> error("unknown item type")
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.card_post -> {
                val binding =
                    CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PostViewHolder(binding, listener)
            }

            R.layout.card_event -> {
                val binding =
                    CardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EventViewHolder(binding, listener)//todo сделать нового или общего слушателя
            }

            R.layout.card_ad -> {
                val binding =
                    CardAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AdViewHolder(binding)
            }

            else -> error("unknown item type: $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is Ad -> (holder as? AdViewHolder)?.bind(item)
            is Post -> (holder as? PostViewHolder)?.bind(item)
            is Event -> (holder as? EventViewHolder)?.bind(item)
            null -> error("unknown item type")
        }
    }
}

