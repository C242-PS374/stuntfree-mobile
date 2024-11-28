package com.capstone.c242_ps374.stuntfree.data.api

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

    @POST("register")
    suspend fun registerUser(
        @Body registerData: RegisterRequest
    ): Response<RegisterResponse>

    @POST("login")
    suspend fun loginUser(
        @Body loginData: LoginRequest
    ): Response<LoginResponse>

    @GET("stories")
    suspend fun getAllDokter(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): Response<ServiceResponse>

}