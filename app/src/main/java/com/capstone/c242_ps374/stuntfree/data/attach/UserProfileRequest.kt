package com.capstone.c242_ps374.stuntfree.data.attach

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfileRequest(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("email") val email: String,
    @field:SerializedName("password") val password: String,
    @field:SerializedName("stage") val stage: String,
    @field:SerializedName("gestasional_age") val gestasionalAge: String,
    @field:SerializedName("child_dob") val childDob: String,
    @field:SerializedName("child_gender") val childGender: String,
    @field:SerializedName("child_born_weight") val childBornWeight: Int,
    @field:SerializedName("child_born_height") val childBornHeight: Int,
    @field:SerializedName("child_height") val childHeight: Int,
    @field:SerializedName("child_weight") val childWeight: Int,
    @field:SerializedName("address") val address: String,
    @field:SerializedName("is_environment_suitable") val isEnvironmentSuitable: Boolean,
    @field:SerializedName("is_nutrition_fulfilled") val isNutritionFulfilled: Boolean
) : Parcelable
