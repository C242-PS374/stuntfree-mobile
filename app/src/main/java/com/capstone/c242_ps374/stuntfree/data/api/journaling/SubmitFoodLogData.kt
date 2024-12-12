package com.capstone.c242_ps374.stuntfree.data.api.journaling

import com.google.gson.annotations.SerializedName
import retrofit2.http.Multipart

data class SubmitFoodLogData(
    @field:SerializedName("foods") val foods: ArrayList<SubmitFoodLog?>,
    @field:SerializedName("name") val name: String?
)