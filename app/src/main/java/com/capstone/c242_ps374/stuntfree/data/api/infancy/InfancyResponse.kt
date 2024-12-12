package com.capstone.c242_ps374.stuntfree.data.api.infancy

import com.google.gson.annotations.SerializedName

data class InfancyResponse(
    @field:SerializedName("data") val data: InfancyData?,
    @field:SerializedName("message") val message: String?
)