package com.capstone.c242_ps374.stuntfree.data.api.get

import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLogResult
import com.capstone.c242_ps374.stuntfree.data.api.scan.FoodItem
import com.google.gson.annotations.SerializedName

data class TodayResponse(
    @field:SerializedName("message") val message: String?,
    @field:SerializedName("result") val result: List<SubmitFoodLogResult>
)