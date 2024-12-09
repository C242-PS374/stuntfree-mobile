package com.capstone.c242_ps374.stuntfree.data.auth

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @field:SerializedName("email") val email: String,
    @field:SerializedName("password") val password: String
)