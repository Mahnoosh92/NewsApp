package com.mahdavi.newsapp.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.data.model.remote.news.HeadlineArticle
import com.mahdavi.newsapp.databinding.ItemNewsBinding

class FavoriteAdapter(private val navigate:(headlineArticle:HeadlineArticle)->Unit) : ListAdapter<HeadlineArticle, RecyclerView.ViewHolder>(Callback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsItemViewHolder(binding, navigate = navigate)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsItemViewHolder).bind(getItem(position))
    }
}

class NewsItemViewHolder(
    private val binding: ItemNewsBinding,
    private val navigate:(headlineArticle:HeadlineArticle)->Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        itemArticleUiState
        : HeadlineArticle
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
            root.setOnClickListener {
                navigate(itemArticleUiState)
            }
        }
    }
}

private class Callback : DiffUtil.ItemCallback<HeadlineArticle>() {

    override fun areItemsTheSame(
        oldItem: HeadlineArticle, newItem: HeadlineArticle
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: HeadlineArticle, newItem: HeadlineArticle
    ): Boolean {
        return oldItem == newItem
    }
}
