package com.capstone.c242_ps374.stuntfree.data.api.journaling

import com.google.gson.annotations.SerializedName

data class SubmitFoodLog(
    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("qty") val qty: Int?
)