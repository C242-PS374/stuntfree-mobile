package com.capstone.c242_ps374.stuntfree.data.api.get

import com.google.gson.annotations.SerializedName

data class PredictResponse(
    @field:SerializedName("message") val message: String?,
    @field:SerializedName("result") val result: String?
)
