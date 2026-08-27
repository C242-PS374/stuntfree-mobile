package com.capstone.c242_ps374.stuntfree.data.api.pregnancy

import com.google.gson.annotations.SerializedName

data class PregnancyData(
    @field:SerializedName("stage") val stage: String?,
    @field:SerializedName("gestasional_age") val gestasionalAge: Int?,
    @field:SerializedName("address") val address: String?,
    @field:SerializedName("is_environment_suitable") val isEnvironmentSuitable: Boolean?,
    @field:SerializedName("is_nutrition_fulfilled") val isNutritionFulfilled: Boolean?,
    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("user_id") val userId: Int?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("created_at") val createdAt: String?,
    @field:SerializedName("updated_at") val updatedAt: String?
)