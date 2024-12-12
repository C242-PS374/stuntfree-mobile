package com.capstone.c242_ps374.stuntfree.data.api.scan

import com.google.gson.annotations.SerializedName

data class FoodItem(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("qty") val qty: Int,
    @field:SerializedName("nutrition") val nutrition: Nutrition
)