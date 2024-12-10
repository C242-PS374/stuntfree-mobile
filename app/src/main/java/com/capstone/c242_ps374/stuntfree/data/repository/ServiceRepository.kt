package com.capstone.c242_ps374.stuntfree.data.repository

import com.capstone.c242_ps374.stuntfree.data.api.ApiService
import com.capstone.c242_ps374.stuntfree.data.service.ServiceResponse
import com.capstone.c242_ps374.stuntfree.data.manager.SessionManager
import javax.inject.Inject

class ServiceRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    suspend fun getServices(): Result<ServiceResponse> {
        val token = sessionManager.getAccessToken()

        return try {
            val response = apiService.getAllDokter(
                token = "Bearer $token",
                page = 1,
                size = 5
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
