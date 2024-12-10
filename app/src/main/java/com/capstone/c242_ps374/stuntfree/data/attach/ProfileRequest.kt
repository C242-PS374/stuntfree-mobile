package com.capstone.c242_ps374.stuntfree.data.attach

import com.google.gson.annotations.SerializedName

data class ProfileRequest(
    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("user_id") val userId: Int?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("created_at") val created: String?,
    @field:SerializedName("updated_at") val updated: String?
)