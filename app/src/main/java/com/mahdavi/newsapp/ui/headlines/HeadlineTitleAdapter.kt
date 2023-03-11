package com.mahdavi.newsapp.ui.headlines

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.data.model.local.HeadlineTitle
import com.mahdavi.newsapp.databinding.ItemHeadlineBinding


class HeadlineTitleAdapter() :
    ListAdapter<HeadlineTitle, RecyclerView.ViewHolder>(DiffCallback()) {
    private var callBackOnTitle: CallBackOnTitle? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemHeadlineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeadlineItemViewHolder(binding, callBackOnTitle)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HeadlineItemViewHolder).bind(getItem(position))
    }

    fun registerCallback(callBackOnTitle: CallBackOnTitle) {
        this.callBackOnTitle = callBackOnTitle
    }
}

class HeadlineItemViewHolder(
    private val binding: ItemHeadlineBinding,
    private val callBackOnTitle: CallBackOnTitle?
) : RecyclerView.ViewHolder(binding.root) {
    private var currentUiState: HeadlineTitle? = null


    fun bind(
        itemArticleUiState
        : HeadlineTitle
    ) {
        currentUiState = itemArticleUiState

        binding.apply {
            //TODO:placeholder and error and loader for coil

            headline.text = itemArticleUiState.title
            if (itemArticleUiState.isSelected) {
                headline.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.sister
                    )
                )
            } else {
                headline.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black_200
                    )
                )
            }
        }

        binding.root.setOnClickListener {
            callBackOnTitle?.titleClicked(itemArticleUiState.title)
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
