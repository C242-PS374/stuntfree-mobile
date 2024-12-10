package com.capstone.c242_ps374.stuntfree.data.attach

import com.google.gson.annotations.SerializedName

data class InfancyData(
    @field:SerializedName("stage") val stage: String = "infancy",
    @field:SerializedName("child_dob") val childDob: String?,
    @field:SerializedName("child_gender") val childGender: String?,
    @field:SerializedName("child_born_weight") val childBornWeight: Int?,
    @field:SerializedName("child_born_height") val childBornHeight: Int?,
    @field:SerializedName("child_height") val childHeight: Int?,
    @field:SerializedName("child_weight") val childWeight: Int?,
    @field:SerializedName("address") val address: String?,
    @field:SerializedName("is_environment_suitable") val isEnvironmentSuitable: Boolean?,
    @field:SerializedName("is_nutrition_fulfilled") val isNutritionFulfilled: Boolean?,
    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("user_id") val userId: Int?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("created_at") val createdAt: String?,
    @field:SerializedName("updated_at") val updatedAt: String?
)