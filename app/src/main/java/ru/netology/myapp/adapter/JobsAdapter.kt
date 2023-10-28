package ru.netology.myapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myapp.R
import ru.netology.myapp.databinding.CardJobsBinding
import ru.netology.myapp.dto.Job

class JobsAdapter(
    private val listener: JobEventListener
) : ListAdapter<Job, JobViewHolder>(JobDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = CardJobsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position)
        holder.bind(job)
    }
}

class JobViewHolder(
    val binding: CardJobsBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(job: Job) {
        binding.apply {
            jobName.text = job.name
            jobPosition.text = job.position
            val workDuration = if (job.finish != null) {
                (job.start + " - " + job.finish)
            } else ("C " + job.start)

            jobDuration.text = workDuration

            menu.isVisible = job.ownedByMe == true
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.job_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {

                                true
                            }

                            R.id.edit -> {
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }

    }
}

class JobDiffCallback() : DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem == newItem
    }
}
