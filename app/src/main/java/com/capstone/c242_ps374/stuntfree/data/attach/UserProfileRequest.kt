package com.capstone.c242_ps374.stuntfree.data.attach

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserProfileRequest(
    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("email") val email: String?,
    @field:SerializedName("created_at") val created: String?,
    @field:SerializedName("updated_at") val updated: String?,
    @field:SerializedName("profile") val profile: ProfileRequest?
)