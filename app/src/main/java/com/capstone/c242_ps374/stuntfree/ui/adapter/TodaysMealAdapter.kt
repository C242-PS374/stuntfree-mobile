package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.databinding.ItemTodaysMealBinding
import com.capstone.c242_ps374.stuntfree.ui.camera.CameraActivity
import com.capstone.c242_ps374.stuntfree.ui.home.TodayLogDetailsActivity

// Data class to represent a meal item
data class TodaysMealItem(
    val title: String,
    val description: String
)

class TodaysMealAdapter(private val items: List<TodaysMealItem>) : RecyclerView.Adapter<TodaysMealAdapter.TodaysMealViewHolder>() {

    inner class TodaysMealViewHolder(private val binding: ItemTodaysMealBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TodaysMealItem) {
            binding.tvTitle.text = item.title
            binding.tvDesc.text = item.description


            binding.editLog.setOnClickListener {
                val context: Context = itemView.context
                val intent = Intent(context, TodayLogDetailsActivity::class.java)
                context.startActivity(intent)
            }

            binding.iconInfo.setOnClickListener {
                // Handle info click
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodaysMealViewHolder {
        val binding = ItemTodaysMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodaysMealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodaysMealViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
