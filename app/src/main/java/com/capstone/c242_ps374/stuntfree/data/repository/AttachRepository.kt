package com.capstone.c242_ps374.stuntfree.data.repository

import com.capstone.c242_ps374.stuntfree.data.api.ApiService
import com.capstone.c242_ps374.stuntfree.data.attach.InfancyRequest
import com.capstone.c242_ps374.stuntfree.data.attach.InfancyResponse
import com.capstone.c242_ps374.stuntfree.data.attach.PregnancyRequest
import com.capstone.c242_ps374.stuntfree.data.attach.PregnancyResponse
import com.capstone.c242_ps374.stuntfree.data.attach.UserProfileResponse
import com.capstone.c242_ps374.stuntfree.data.manager.SessionManager
import retrofit2.Response
import javax.inject.Inject

class AttachRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    private suspend fun <T> makeApiRequest(
        apiCall: suspend () -> Response<T>
    ): Result<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("Request failed: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Request failed: ${e.message}"))
        }
    }

    private fun getAuthToken(): String {
        val token = sessionManager.getAuthToken()
        if (token.isNullOrEmpty()) {
            throw Exception("Token not found")
        }
        return "Bearer $token"
    }

    suspend fun attachInfancyProfile(body: InfancyRequest): Result<InfancyResponse> {
        val token = getAuthToken()

        return makeApiRequest {
            apiService.attachInfancyProfile(
                token = token,
                body = body
            )
        }
    }

    suspend fun attachPregnancyProfile(body: PregnancyRequest): Result<PregnancyResponse> {
        val token = getAuthToken()

        return makeApiRequest {
            apiService.attachPregnancyProfile(
                token = token,
                body = body
            )
        }
    }

    suspend fun getProfile(): Result<UserProfileResponse> {
        val token = sessionManager.getAuthToken() ?: return Result.failure(Exception("Token not found"))

        return makeApiRequest {
            apiService.getUserProfile(
                token = "Bearer $token"
            )
        }
    }
}