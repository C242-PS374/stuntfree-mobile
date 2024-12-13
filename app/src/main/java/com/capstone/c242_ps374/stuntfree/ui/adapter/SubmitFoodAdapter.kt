package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLog

class SubmitFoodAdapter(private val submitData: List<SubmitFoodLog>) : RecyclerView.Adapter<SubmitFoodAdapter.FoodGroupViewHolder>() {

    inner class FoodGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupName: TextView = itemView.findViewById(R.id.foodGroupName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_group, parent, false)
        return FoodGroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodGroupViewHolder, position: Int) {
        val foodItem = submitData[position]
        holder.groupName.text = foodItem.name
    }

    override fun getItemCount() = submitData.size
}