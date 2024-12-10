package com.capstone.c242_ps374.stuntfree.data.attach

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @field:SerializedName("data") val data: UserProfileRequest?,
    @field:SerializedName("message") val message: String?
)