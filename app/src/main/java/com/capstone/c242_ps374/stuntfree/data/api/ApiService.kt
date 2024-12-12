package com.capstone.c242_ps374.stuntfree.data.api

import com.capstone.c242_ps374.stuntfree.data.api.infancy.InfancyRequest
import com.capstone.c242_ps374.stuntfree.data.api.infancy.InfancyResponse
import com.capstone.c242_ps374.stuntfree.data.api.pregnancy.PregnancyRequest
import com.capstone.c242_ps374.stuntfree.data.api.pregnancy.PregnancyResponse
import com.capstone.c242_ps374.stuntfree.data.api.attach.UserProfileResponse
import com.capstone.c242_ps374.stuntfree.data.api.auth.LoginRequest
import com.capstone.c242_ps374.stuntfree.data.api.auth.LoginResponse
import com.capstone.c242_ps374.stuntfree.data.api.auth.RegisterRequest
import com.capstone.c242_ps374.stuntfree.data.api.auth.RegisterResponse
import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLogData
import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLogResponse
import com.capstone.c242_ps374.stuntfree.data.api.scan.FoodScanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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
        @Header("Authorization") token: String,
    ): Response<UserProfileResponse>

    @Multipart
    @POST("journalling/food/submit-log")
    suspend fun submitFoodLog(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("foods") foods: RequestBody
    ): Response<SubmitFoodLogResponse>

    @Multipart
    @POST("journalling/food/scan")
    suspend fun scanFood(
        @Header("Authorization") authHeader: String,
        @Part file: MultipartBody.Part
    ): Response<FoodScanResponse>
}