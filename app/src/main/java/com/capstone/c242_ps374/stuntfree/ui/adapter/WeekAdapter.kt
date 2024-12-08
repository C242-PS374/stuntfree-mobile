package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.data.model.DayItem
import com.capstone.c242_ps374.stuntfree.databinding.ItemDayBinding

class WeekAdapter : ListAdapter<DayItem, WeekAdapter.WeekViewHolder>(DIFF_CALLBACK) {

    inner class WeekViewHolder(private val binding: ItemDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dayItem: DayItem) {
            // Atur nama hari (huruf awal)
            binding.tvDayInitial.text = dayItem.dayName.substring(0, 1)

            // Atur tanggal
            binding.tvDate.text = dayItem.date.substring(0, 2) // Hanya angka tanggal

            // Atur border (hanya untuk hari ini)
            if (dayItem.isSelected) {
                binding.tvDate.setBackgroundResource(R.drawable.bg_date_circle)
                binding.tvDate.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
            } else {
                binding.tvDate.setBackgroundResource(0)
                binding.tvDate.setTextColor(ContextCompat.getColor(binding.root.context, R.color.neutral_900))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeekViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DayItem>() {
            override fun areItemsTheSame(oldItem: DayItem, newItem: DayItem): Boolean {
                // Gunakan properti unik sebagai pembanding, misalnya `date`
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: DayItem, newItem: DayItem): Boolean {
                // Periksa apakah konten item sama
                return oldItem == newItem
            }
        }

    }
}

