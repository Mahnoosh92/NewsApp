package com.mahdavi.newsapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mahdavi.newsapp.data.model.remote.ArticleResponse
import com.mahdavi.newsapp.databinding.ItemNewsBinding

class HomeNewsAdapter(private val update: (ArticleResponse) -> Unit) :
    ListAdapter<ArticleResponse, RecyclerView.ViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsItemViewHolder(binding, update)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsItemViewHolder).bind(getItem(position))
    }
}

class NewsItemViewHolder(private val binding: ItemNewsBinding, update: (ArticleResponse) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    var currentArticle: ArticleResponse? = null

    init {
        binding.myCard.setOnClickListener {
            currentArticle?.let {
                update(it)
            }
        }
    }

    fun bind(article: ArticleResponse) {
        currentArticle = article
        binding.apply {
            imageId.load(article.media)
            title.text = article.title
            summary.text = article.summary
        }
    }
}

private class PlantDiffCallback : DiffUtil.ItemCallback<ArticleResponse>() {

    override fun areItemsTheSame(oldItem: ArticleResponse, newItem: ArticleResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ArticleResponse, newItem: ArticleResponse): Boolean {
        return oldItem == newItem
    }
}
