package com.capstone.c242_ps374.stuntfree.data.api.auth

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("error") val error: Boolean,
    @field:SerializedName("message") val message: String,
)
