package com.capstone.c242_ps374.stuntfree.data.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("message") val message: String,
    @field:SerializedName("token") val token: LoginResult
)