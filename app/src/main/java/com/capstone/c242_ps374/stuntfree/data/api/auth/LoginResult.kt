package com.capstone.c242_ps374.stuntfree.data.api.auth

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @field:SerializedName("token_type") val tokenType: String,
    @field:SerializedName("access_token") val accessToken: String,
    @field:SerializedName("refresh_token") val refreshToken: String
)