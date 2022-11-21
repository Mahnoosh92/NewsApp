package com.mahdavi.newsapp.ui.home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mahdavi.newsapp.data.model.remote.ArticleResponse
import com.mahdavi.newsapp.databinding.NewsItemLayoutBinding

class HomeNewsAdapter : ListAdapter<ArticleResponse, RecyclerView.ViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            NewsItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsItemViewHolder).bind(getItem(position))
    }

}

class NewsItemViewHolder(private val binding: NewsItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(article: ArticleResponse) {
        binding.apply {
            newsTitle.text = article.title
            newsImage.load(article.media)
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
