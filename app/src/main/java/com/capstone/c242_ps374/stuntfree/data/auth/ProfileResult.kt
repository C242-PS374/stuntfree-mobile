package com.capstone.c242_ps374.stuntfree.data.auth

import com.google.gson.annotations.SerializedName

data class ProfileResult(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("email") val email: String
)