package com.capstone.c242_ps374.stuntfree.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.c242_ps374.stuntfree.R
import com.capstone.c242_ps374.stuntfree.data.api.scan.FoodItem

class FoodGroupAdapter(private val foodData: List<FoodItem>) : RecyclerView.Adapter<FoodGroupAdapter.FoodGroupViewHolder>() {

    inner class FoodGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupName: TextView = itemView.findViewById(R.id.foodGroupName)
        val nutrientList: RecyclerView = itemView.findViewById(R.id.nutrientList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_group, parent, false)
        return FoodGroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodGroupViewHolder, position: Int) {
        val foodItem = foodData[position]
        holder.groupName.text = "${foodItem.name} (Qty: ${foodItem.qty})"

        val nutrientAdapter = FoodNutrientAdapter(foodItem.nutrition)
        holder.nutrientList.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = nutrientAdapter
            setHasFixedSize(true)
        }
    }

    override fun getItemCount() = foodData.size
}
