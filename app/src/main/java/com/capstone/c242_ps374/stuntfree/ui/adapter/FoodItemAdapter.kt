package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLog
import com.capstone.c242_ps374.stuntfree.databinding.ItemFoodBinding

class FoodItemAdapter(foods: List<SubmitFoodLog>?) : ListAdapter<SubmitFoodLog, FoodItemAdapter.FoodItemViewHolder>(FoodItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val food = getItem(position) // Menggunakan getItem() untuk ListAdapter
        food?.let { holder.bind(it) }
    }

    inner class FoodItemViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: SubmitFoodLog) {
            binding.foodName.text = food.name
            binding.foodQty.text = "Qty: ${food.qty}"
        }
    }

    // DiffUtil untuk membandingkan item lama dan baru
    class FoodItemDiffCallback : DiffUtil.ItemCallback<SubmitFoodLog>() {
        override fun areItemsTheSame(oldItem: SubmitFoodLog, newItem: SubmitFoodLog): Boolean {
            return oldItem.id == newItem.id // Bandingkan ID untuk memastikan item yang sama
        }

        override fun areContentsTheSame(oldItem: SubmitFoodLog, newItem: SubmitFoodLog): Boolean {
            return oldItem == newItem // Bandingkan seluruh konten item
        }
    }
}
