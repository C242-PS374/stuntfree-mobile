package com.capstone.c242_ps374.stuntfree.data.attach

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PregnancyRequest(
    @field:SerializedName("stage") val stage: String = "pregnancy",
    @field:SerializedName("gestasional_age") val gestasionalAge: Int,
    @field:SerializedName("address") val address: String,
    @field:SerializedName("is_environment_suitable") val isEnvironmentSuitable: Boolean,
    @field:SerializedName("is_nutrition_fulfilled") val isNutritionFulfilled: Boolean
) : Parcelable