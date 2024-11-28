package com.capstone.c242_ps374.stuntfree.data.service

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Service(
    @field:SerializedName("id") val id: String? = null,
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("description") val description: String? = null,
    @field:SerializedName("photoUrl") val photoUrl: String? = null,
    @field:SerializedName("createdAt") val createdAt: String? = null,
    @field:SerializedName("lat") val lat: Double? = null,
    @field:SerializedName("lon") val lon: Double? = null,
    @field:SerializedName("distance") val distance: Double = 0.0
) : Parcelable