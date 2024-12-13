package com.capstone.c242_ps374.stuntfree.data.api.journaling

import com.google.gson.annotations.SerializedName

data class SubmitFoodLogRequest(
    @field:SerializedName("img_url") val imgUrl: String?,
    @field:SerializedName("foods") val foods: List<SubmitFoodLog>?
)
