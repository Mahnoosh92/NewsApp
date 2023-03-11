package com.mahdavi.newsapp.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.data.model.remote.news.SearchedArticle
import com.mahdavi.newsapp.databinding.ItemNewsBinding

class SearchAdapter : ListAdapter<SearchedArticle, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsItemViewHolder).bind(getItem(position))
    }
}

class NewsItemViewHolder(
    private val binding: ItemNewsBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        itemArticleUiState
        : SearchedArticle
    ) {


        binding.apply {
            //TODO:placeholder and error and loader for coil
            imageId.load(
                itemArticleUiState.media
            ) {
                crossfade(true)
                placeholder(R.drawable.newspaper)

            }
            title.text = itemArticleUiState.title
            summary.text = itemArticleUiState.summary
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<SearchedArticle>() {

    override fun areItemsTheSame(
        oldItem: SearchedArticle, newItem: SearchedArticle
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SearchedArticle, newItem: SearchedArticle
    ): Boolean {
        return oldItem == newItem
    }
}
