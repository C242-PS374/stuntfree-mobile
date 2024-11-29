package com.capstone.c242_ps374.stuntfree.data.auth

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String
)
