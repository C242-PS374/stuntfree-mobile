package com.capstone.c242_ps374.stuntfree.data.api.journaling

import com.google.gson.annotations.SerializedName

data class SubmitFoodLogResponse(
    @field:SerializedName("message") val message: String?,
    @field:SerializedName("result") val result: ArrayList<SubmitFoodLog?>
)