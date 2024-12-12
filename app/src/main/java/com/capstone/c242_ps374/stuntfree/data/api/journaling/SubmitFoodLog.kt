package com.capstone.c242_ps374.stuntfree.data.api.journaling

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubmitFoodLog(
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("qty") val qty: Int?
): Parcelable