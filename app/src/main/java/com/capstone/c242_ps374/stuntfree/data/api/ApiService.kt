package com.capstone.c242_ps374.stuntfree.data.api

import com.capstone.c242_ps374.stuntfree.data.attach.InfancyRequest
import com.capstone.c242_ps374.stuntfree.data.attach.InfancyResponse
import com.capstone.c242_ps374.stuntfree.data.attach.PregnancyRequest
import com.capstone.c242_ps374.stuntfree.data.attach.PregnancyResponse
import com.capstone.c242_ps374.stuntfree.data.attach.UserProfileResponse
import com.capstone.c242_ps374.stuntfree.data.auth.LoginRequest
import com.capstone.c242_ps374.stuntfree.data.auth.LoginResponse
import com.capstone.c242_ps374.stuntfree.data.auth.RegisterRequest
import com.capstone.c242_ps374.stuntfree.data.auth.RegisterResponse
import com.capstone.c242_ps374.stuntfree.data.service.ServiceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("auth/signup")
    suspend fun registerUser(
        @Body registerData: RegisterRequest
    ): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun loginUser(
        @Body loginData: LoginRequest
    ): Response<LoginResponse>

    @POST("user/me/attach-profile")
    suspend fun attachInfancyProfile(
        @Header("Authorization") token: String,
        @Body body: InfancyRequest
    ): Response<InfancyResponse>

    @POST("user/me/attach-profile")
    suspend fun attachPregnancyProfile(
        @Header("Authorization") token: String,
        @Body body: PregnancyRequest
    ): Response<PregnancyResponse>

    @GET("user/me")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<UserProfileResponse>
}