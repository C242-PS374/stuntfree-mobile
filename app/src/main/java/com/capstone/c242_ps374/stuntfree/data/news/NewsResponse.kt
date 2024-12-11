package com.capstone.c242_ps374.stuntfree.data.attach

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @field:SerializedName("status") val status: String?,
    @field:SerializedName("totalResults") val totalResults: String?,
    @field:SerializedName("articels") val articels: String?,
)