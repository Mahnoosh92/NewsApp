package com.mahdavi.newsapp.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahdavi.newsapp.databinding.SettingItemBinding

class SettingDataAdapter(val data: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = SettingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data.get(position))
    }

    override fun getItemCount() = data.size
}
class ViewHolder(val binding:SettingItemBinding):RecyclerView.ViewHolder(binding.root) {
    fun bind(text:String) {
        binding.settingText.text = text
    }
}