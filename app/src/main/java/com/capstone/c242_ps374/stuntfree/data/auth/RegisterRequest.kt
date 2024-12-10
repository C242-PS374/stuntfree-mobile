package com.capstone.c242_ps374.stuntfree.data.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterRequest(
    @field:SerializedName("email") val email: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("password") val password: String,
    @field:SerializedName("confirm_password") val confirmPassword: String,
): Parcelable