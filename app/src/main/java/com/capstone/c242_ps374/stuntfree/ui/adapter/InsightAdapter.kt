package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.c242_ps374.stuntfree.data.service.Service
import com.capstone.c242_ps374.stuntfree.databinding.ItemInsightBinding

class InsightAdapter : ListAdapter<Service, InsightAdapter.ServiceViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemInsightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = getItem(position)
        holder.bind(service)
    }

    class ServiceViewHolder(
        private val binding: ItemInsightBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(service: Service?) {
            service?.let {
                binding.apply {
                    tvTitle.text = it.description ?: "Unknown"
                    btnTitle.text = it.name ?: "Unknown"
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