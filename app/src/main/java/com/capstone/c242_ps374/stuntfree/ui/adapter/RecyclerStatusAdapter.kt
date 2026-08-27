package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.c242_ps374.stuntfree.R

class RecyclerStatusAdapter(private val statusList: List<StatusItem>) : RecyclerView.Adapter<RecyclerStatusAdapter.StatusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_status, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val statusItem = statusList[position]
        holder.bind(statusItem)
    }

    override fun getItemCount(): Int {
        return statusList.size
    }

    inner class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvStuntingStatus: TextView = itemView.findViewById(R.id.tv_stunting_status)
        private val tvNutritionIntake: TextView = itemView.findViewById(R.id.tv_nutriotion_intake)
        private val tvEnvironmentalConditions: TextView = itemView.findViewById(R.id.tv_environmental_conditions)

        fun bind(statusItem: StatusItem) {
            tvTitle.text = statusItem.title
            tvStuntingStatus.text = statusItem.stuntingDescription
            tvNutritionIntake.text = statusItem.nutritionDescription
            tvEnvironmentalConditions.text = statusItem.environmentalDescription
        }
    }

    data class StatusItem(
        val title: String,
        val stuntingDescription: String,
        val nutritionDescription: String,
        val environmentalDescription: String
    )
}