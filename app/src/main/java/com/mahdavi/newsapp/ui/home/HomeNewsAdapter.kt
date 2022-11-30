package com.mahdavi.newsapp.ui.home

import android.provider.SyncStateContract.Helpers.update
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.data.model.remote.ArticleResponse
import com.mahdavi.newsapp.databinding.ItemNewsBinding

class HomeNewsAdapter(private val onClick: (ArticleResponse) -> Unit) :
    ListAdapter<ItemArticleUiState, RecyclerView.ViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsItemViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsItemViewHolder).bind(getItem(position))
    }
}

class NewsItemViewHolder(
    private val binding: ItemNewsBinding,
    onClick: (ArticleResponse) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    private var currentUiState: ItemArticleUiState? = null

    init {
        binding.myCard.setOnClickListener {
            currentUiState?.let {
                it.onClick(it.article)
                onClick(it.article)
            }
        }
    }

    fun bind(
        itemArticleUiState
        : ItemArticleUiState
    ) {
        currentUiState = itemArticleUiState

        binding.apply {
            //TODO:placeholder and error and loader for coil
            imageId.load(
                itemArticleUiState
                    .article.media
            ){
                crossfade(true)
                placeholder(R.drawable.news)

            }
            title.text = itemArticleUiState
                .article.title
            summary.text = itemArticleUiState
                .article.summary
        }
    }
}

private class PlantDiffCallback : DiffUtil.ItemCallback<ItemArticleUiState>() {

    override fun areItemsTheSame(
        oldItem: ItemArticleUiState, newItem: ItemArticleUiState
    ): Boolean {
        return oldItem.article.id == newItem.article.id
    }

    override fun areContentsTheSame(
        oldItem: ItemArticleUiState, newItem: ItemArticleUiState
    ): Boolean {
        return oldItem == newItem
    }
}
