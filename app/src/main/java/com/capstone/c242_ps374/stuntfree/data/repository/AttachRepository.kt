package com.capstone.c242_ps374.stuntfree.data.repository

import android.util.Log
import com.capstone.c242_ps374.stuntfree.data.api.ApiService
import com.capstone.c242_ps374.stuntfree.data.api.infancy.InfancyRequest
import com.capstone.c242_ps374.stuntfree.data.api.infancy.InfancyResponse
import com.capstone.c242_ps374.stuntfree.data.api.pregnancy.PregnancyRequest
import com.capstone.c242_ps374.stuntfree.data.api.pregnancy.PregnancyResponse
import com.capstone.c242_ps374.stuntfree.data.api.attach.UserProfileResponse
import com.capstone.c242_ps374.stuntfree.data.api.get.PredictResponse
import com.capstone.c242_ps374.stuntfree.data.api.get.TodayResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class AttachRepository @Inject constructor(
    @Named("stuntingApiService") private val apiService: ApiService,
) {

    suspend fun predictStunting(token: String): Result<PredictResponse> {
        return try {
            if (token.isEmpty()) {
                return Result.failure(Exception("Token is missing"))
            }

            makeApiRequest { apiService.predictStunting("Bearer $token") }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun todayLog(token: String): Result<TodayResponse> {
        return try {
            if (token.isEmpty()) {
                return Result.failure(Exception("Token is missing"))
            }

            makeApiRequest { apiService.todayLog("Bearer $token") }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
                    Log.d("API Response", "Success: $it")
                    Result.success(it)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                val errorCode = response.code()
                Log.e("API Error", "Code: $errorCode, Message: $errorMessage")
                Result.failure(Exception("API Error: $errorMessage (Code: $errorCode)"))
            }
        } catch (e: Exception) {
            Log.e("API Request Failed", "Error: ${e.message}")
            Result.failure(Exception("Request failed: ${e.message}"))
        }
    }
}