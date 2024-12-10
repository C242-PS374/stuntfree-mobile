package com.capstone.c242_ps374.stuntfree.data.attach

import com.google.gson.annotations.SerializedName

data class PregnancyResponse(
    @field:SerializedName("data") val data: PregnancyData?,
    @field:SerializedName("message") val message: String?
)