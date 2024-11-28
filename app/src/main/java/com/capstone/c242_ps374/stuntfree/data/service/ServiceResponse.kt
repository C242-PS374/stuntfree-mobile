package com.capstone.c242_ps374.stuntfree.data.service

import com.google.gson.annotations.SerializedName

data class ServiceResponse(
    @field:SerializedName("error") val error: Boolean,
    @field:SerializedName("message") val message: String,
    @field:SerializedName("listStory") val listStory: List<Service>
)