package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.c242_ps374.stuntfree.data.service.Service
import com.capstone.c242_ps374.stuntfree.databinding.ItemHomeBinding
import com.capstone.c242_ps374.stuntfree.databinding.ItemInsightBinding

class HomeAdapter : ListAdapter<Service, HomeAdapter.HomeViewHolder>(DIFF_CALLBACK) {

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

        fun bind(service: Service?) {
            service?.let {
                binding.apply {
                    tvTitle.text = it.description ?: "Unknown"
                    Glide.with(itemView.context)
                        .load(it.photoUrl)
//                        .placeholder(R.drawable.placeholder_image) // Tambahkan drawable sesuai
//                        .error(R.drawable.error_image) // Tambahkan drawable sesuai
                        .into(ivImage)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Service>() {
            override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean {
                return oldItem == newItem
            }
        }
    }
}