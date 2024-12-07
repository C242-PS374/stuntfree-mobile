package com.capstone.c242_ps374.stuntfree.data.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("error") val error: Boolean,
    @field:SerializedName("message") val message: String,
    @field:SerializedName("token") val token: String,
    @field:SerializedName("stage") val stage: String?,
    @field:SerializedName("loginResult") val loginResult: LoginResult
)