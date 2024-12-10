package com.capstone.c242_ps374.stuntfree.data.repository

import android.util.Log
import com.capstone.c242_ps374.stuntfree.data.api.ApiService
import com.capstone.c242_ps374.stuntfree.data.attach.InfancyRequest
import com.capstone.c242_ps374.stuntfree.data.attach.InfancyResponse
import com.capstone.c242_ps374.stuntfree.data.attach.PregnancyRequest
import com.capstone.c242_ps374.stuntfree.data.attach.PregnancyResponse
import com.capstone.c242_ps374.stuntfree.data.attach.UserProfileRequest
import com.capstone.c242_ps374.stuntfree.data.attach.UserProfileResponse
import com.capstone.c242_ps374.stuntfree.data.manager.SessionManager
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject

class AttachRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun attachInfancyProfile(token: String, body: InfancyRequest): Result<InfancyResponse> {
        return try {
            if (token.isEmpty()) {
                return Result.failure(Exception("Token is missing"))
            }

            makeApiRequest { apiService.attachInfancyProfile("Bearer $token", body) }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun attachPregnancyProfile(token: String, body: PregnancyRequest): Result<PregnancyResponse> {
        return try {
            if (token.isEmpty()) {
                return Result.failure(Exception("Token is missing"))
            }

            makeApiRequest { apiService.attachPregnancyProfile("Bearer $token", body) }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfile(token: String): Result<UserProfileResponse> {
        return try {
            if (token.isEmpty()) {
                return Result.failure(Exception("Token is missing"))
            }

            makeApiRequest { apiService.getUserProfile("Bearer $token") }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun <T> makeApiRequest(apiCall: suspend () -> Response<T>): Result<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("API Error: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Request failed: ${e.message}"))
        }
    }
}