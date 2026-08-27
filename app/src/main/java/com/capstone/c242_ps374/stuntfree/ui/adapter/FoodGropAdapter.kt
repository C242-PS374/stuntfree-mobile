package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLogResult
import com.capstone.c242_ps374.stuntfree.databinding.ItemFoodLogBinding

class FoodGropAdapter : ListAdapter<SubmitFoodLogResult, FoodGropAdapter.FoodLogViewHolder>(FoodLogDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodLogViewHolder {
        val binding = ItemFoodLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodLogViewHolder, position: Int) {
        val todayLog = getItem(position) // getItem() digunakan untuk ListAdapter
        todayLog?.let { holder.bind(it) }
    }

    inner class FoodLogViewHolder(private val binding: ItemFoodLogBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todayLog: SubmitFoodLogResult) {
            // Memuat gambar dengan Glide
            Glide.with(binding.imgFoodLog.context)
                .load(todayLog.imgUrl)
                .into(binding.imgFoodLog)

            // Menampilkan waktu dibuat
            binding.createdAt.text = todayLog.createdAt

            // Menampilkan makanan menggunakan RecyclerView di dalam item log
            binding.foodsRecyclerView.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = FoodItemAdapter(todayLog.foods)
            }
        }
    }

    class FoodLogDiffCallback : DiffUtil.ItemCallback<SubmitFoodLogResult>() {
        override fun areItemsTheSame(oldItem: SubmitFoodLogResult, newItem: SubmitFoodLogResult): Boolean {
            return oldItem.id == newItem.id // Periksa ID agar bisa membandingkan item
        }

        override fun areContentsTheSame(oldItem: SubmitFoodLogResult, newItem: SubmitFoodLogResult): Boolean {
            return oldItem == newItem // Membandingkan seluruh konten item
        }
    }
}
