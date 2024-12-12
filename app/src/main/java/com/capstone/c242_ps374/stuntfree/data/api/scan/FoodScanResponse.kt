package com.capstone.c242_ps374.stuntfree.data.api.scan

import com.google.gson.annotations.SerializedName

data class FoodScanResponse(
    @field:SerializedName("message") val message: String,
    @field:SerializedName("result") val result: List<FoodItem>
)