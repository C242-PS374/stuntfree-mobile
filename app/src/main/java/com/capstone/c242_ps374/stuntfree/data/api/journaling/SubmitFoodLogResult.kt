package com.capstone.c242_ps374.stuntfree.data.api.journaling

import com.google.gson.annotations.SerializedName

data class SubmitFoodLogResult(
    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("is_akg_fulfilled") val isAkgFulfilled: Boolean?,
    @field:SerializedName("user_id") val userId: Int?,
    @field:SerializedName("img_url") val imgUrl: String?,
    @field:SerializedName("created_at") val createdAt: String?,
    @field:SerializedName("foods") val foods: List<SubmitFoodLog>?
)