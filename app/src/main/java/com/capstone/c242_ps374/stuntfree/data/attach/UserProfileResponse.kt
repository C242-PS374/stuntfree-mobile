package com.capstone.c242_ps374.stuntfree.data.attach

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("stage") val stage: String
)