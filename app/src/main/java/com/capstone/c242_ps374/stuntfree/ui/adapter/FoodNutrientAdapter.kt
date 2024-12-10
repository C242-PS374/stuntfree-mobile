package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.c242_ps374.stuntfree.R

class FoodNutrientAdapter(private val nutrients: List<String>) : RecyclerView.Adapter<FoodNutrientAdapter.FoodNutrientViewHolder>() {

    inner class FoodNutrientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nutrientText: TextView = itemView.findViewById(R.id.foodInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodNutrientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_info, parent, false)
        return FoodNutrientViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodNutrientViewHolder, position: Int) {
        holder.nutrientText.text = nutrients[position]
    }

    override fun getItemCount() = nutrients.size
}
