package com.mahdavi.newsapp.ui.headlines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.data.model.HeadlineArticle
import com.mahdavi.newsapp.databinding.ItemNewsBinding

class HomeNewsCallbackAdapter(private val callBack: OnClickListener?) :
    ListAdapter<ItemArticleUiState, RecyclerView.ViewHolder>(DiffCallback1()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsItemViewHolder1(binding, callBack)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsItemViewHolder1).bind(getItem(position))
    }
}

class NewsItemViewHolder1(
    private val binding: ItemNewsBinding,
    private val callBack: OnClickListener?
) : RecyclerView.ViewHolder(binding.root) {
    private var currentUiState: ItemArticleUiState? = null

    init {
        binding.myCard.setOnClickListener {
            currentUiState?.let {
                callBack?.onClick(it.article)
            }
        }
    }

    fun bind(
        itemArticleUiState: ItemArticleUiState
    ) {
        currentUiState = itemArticleUiState

        binding.apply {
            //TODO:placeholder and error and loader for coil
            imageId.load(
                itemArticleUiState.article.media
            ) {
                crossfade(true)
                placeholder(R.drawable.news)

            }
            title.text = itemArticleUiState.article.title
            summary.text = itemArticleUiState.article.summary
        }
    }
}

private class DiffCallback1 : DiffUtil.ItemCallback<ItemArticleUiState>() {

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

interface OnClickListener {
    fun onClick(articleResponse: HeadlineArticle)
}