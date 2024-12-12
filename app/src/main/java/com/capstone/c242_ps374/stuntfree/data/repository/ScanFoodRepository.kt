package com.capstone.c242_ps374.stuntfree.data.repository

import android.util.Log
import com.capstone.c242_ps374.stuntfree.data.api.ApiService
import com.capstone.c242_ps374.stuntfree.data.api.scan.FoodScanResponse
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class ScanFoodRepository @Inject constructor(
    @Named("stuntingApiService") private val apiService: ApiService
) {

    suspend fun scanFood(
        token: String,
        file: MultipartBody.Part
    ): Result<FoodScanResponse> {
        return try {
            if (token.isEmpty()) {
                return Result.failure(Exception("Token is missing"))
            }

            makeApiRequest { apiService.scanFood("Bearer $token", file) }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    private suspend fun <T> makeApiRequest(apiCall: suspend () -> Response<T>): Result<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("ScanFoodRepository", "Response Body: $body")

                body?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("ScanFoodRepository", "Error Body: $errorBody")
                Result.failure(Exception("API Error: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("ScanFoodRepository", "Exception: ${e.message}", e)
            Result.failure(e)
        }
    }
}