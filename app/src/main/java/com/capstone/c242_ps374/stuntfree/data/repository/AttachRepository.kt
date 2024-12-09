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
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    private suspend fun getToken(): String {
        val token = sessionManager.getAccessToken().first()
        if (token.isNullOrEmpty()) {
            throw TokenNotFoundException("Token tidak ditemukan. Silakan login kembali.")
        }
        return token
    }

    suspend fun attachInfancyProfile(body: InfancyRequest): Result<InfancyResponse> {
        return try {
            val token = getToken()
            makeApiRequest { apiService.attachInfancyProfile(token = token, body = body) }
        } catch (e: TokenNotFoundException) {
            Result.failure(e)
        }
    }

    suspend fun attachPregnancyProfile(body: PregnancyRequest): Result<PregnancyResponse> {
        return try {
            val token = getToken()
            makeApiRequest { apiService.attachPregnancyProfile(token = token, body = body) }
        } catch (e: TokenNotFoundException) {
            Result.failure(e)
        }
    }

    suspend fun getProfile(): Result<UserProfileResponse> {
        return try {
            val token = getToken()
            makeApiRequest { apiService.getUserProfile(token = token) }
        } catch (e: TokenNotFoundException) {
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

    class TokenNotFoundException(message: String) : Exception(message)
}