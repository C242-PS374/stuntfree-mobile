package com.capstone.c242_ps374.stuntfree.data.repository

import android.util.Log
import com.capstone.c242_ps374.stuntfree.data.api.ApiService
import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLogResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class SubmitFoodRepository @Inject constructor(
    @Named("stuntingApiService") private val apiService: ApiService
) {

    suspend fun submitFoodLog(
        token: String,
        file: MultipartBody.Part,
        foods: RequestBody
    ): Result<SubmitFoodLogResponse> {
        return try {
            if (token.isEmpty()) {
                return Result.failure(Exception("Token is missing"))
            }

            makeApiRequest { apiService.submitFoodLog("Bearer $token", file, foods) }
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
