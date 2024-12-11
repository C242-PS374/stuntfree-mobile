package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.c242_ps374.stuntfree.data.news.Article
import com.capstone.c242_ps374.stuntfree.databinding.ItemHomeBinding

class HomeAdapter : ListAdapter<Article, HomeAdapter.HomeViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HomeViewHolder(
        private val binding: ItemHomeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article?) {
            article?.let {
                binding.apply {
                    tvTitle.text = it.title ?: "No Title Available"
                    tvDesc.text = it.description ?: "No Description Available"
                    Glide.with(itemView.context)
                        .load(it.urlToImage)
                        .into(ivImage)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url  // Assuming URL is unique for each article
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }
}
