package com.capstone.c242_ps374.stuntfree.data.api

import com.capstone.c242_ps374.stuntfree.data.auth.LoginRequest
import com.capstone.c242_ps374.stuntfree.data.auth.LoginResponse
import com.capstone.c242_ps374.stuntfree.data.auth.RegisterRequest
import com.capstone.c242_ps374.stuntfree.data.auth.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("register")
    suspend fun registerUser(
        @Body registerData: RegisterRequest
    ): Response<RegisterResponse>

    @POST("login")
    suspend fun loginUser(
        @Body loginData: LoginRequest
    ): Response<LoginResponse>

}