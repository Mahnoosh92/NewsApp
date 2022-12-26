package com.mahdavi.newsapp.ui.headlines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.data.model.local.HeadlineTitle
import com.mahdavi.newsapp.databinding.ItemHeadlineBinding


class HeadlineTitleAdapter(private val onClick: (HeadlineTitle) -> Unit) :
    ListAdapter<HeadlineTitle, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemHeadlineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeadlineItemViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HeadlineItemViewHolder).bind(getItem(position))
    }
}

class HeadlineItemViewHolder(
    private val binding: ItemHeadlineBinding,
    onClick: (HeadlineTitle) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private var currentUiState: HeadlineTitle? = null

    init {
        binding.headline.setOnClickListener {
            currentUiState?.let {
                onClick(it)
            }
        }
    }

    fun bind(
        itemArticleUiState
        : HeadlineTitle
    ) {
        currentUiState = itemArticleUiState

        binding.apply {
            //TODO:placeholder and error and loader for coil

            headlineTitle.text = itemArticleUiState.title
            if (itemArticleUiState.isSelected) {
                headline.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.purple_500
                    )
                )
            }
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<HeadlineTitle>() {

    override fun areItemsTheSame(
        oldItem: HeadlineTitle, newItem: HeadlineTitle
    ): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(
        oldItem: HeadlineTitle, newItem: HeadlineTitle
    ): Boolean {
        return oldItem == newItem
    }
}
