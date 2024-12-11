package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.c242_ps374.stuntfree.data.news.Article
import com.capstone.c242_ps374.stuntfree.databinding.ItemButtonBinding

class ButtonAdapter(
    private val onButtonClick: (Article) -> Unit
) : ListAdapter<Article, ButtonAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    private val selectedButton = MutableLiveData<String?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding, onButtonClick, selectedButton)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }

    class ArticleViewHolder(
        private val binding: ItemButtonBinding,
        private val onButtonClick: (Article) -> Unit,
        private val selectedButton: LiveData<String?>
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article?) {
            article?.let {
                binding.apply {
                    btnTitleAtas.text = it.author ?: "Unknown"

                    val isSelected = it.author == selectedButton.value

                    btnTitleAtas.setOnClickListener {
                        btnTitleAtas.isSelected = isSelected
                        onButtonClick(article)
                    }
                }
            }
        }
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.source?.id == newItem.source?.id
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }
}