package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.data.api.scan.Nutrition

class FoodNutrientAdapter(nutrients: Nutrition) : RecyclerView.Adapter<FoodNutrientAdapter.FoodNutrientViewHolder>() {

    private val nutrientList = nutrients.toNutritionMap().toList() // Konversi map menjadi list

    inner class FoodNutrientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val labelText: TextView = itemView.findViewById(R.id.label)
        val nutrientText: TextView = itemView.findViewById(R.id.foodInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodNutrientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_info, parent, false)
        return FoodNutrientViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodNutrientViewHolder, position: Int) {
        val (name, value) = nutrientList[position]
        holder.labelText.text = name
        holder.nutrientText.text = value
    }

    override fun getItemCount() = nutrientList.size
}
