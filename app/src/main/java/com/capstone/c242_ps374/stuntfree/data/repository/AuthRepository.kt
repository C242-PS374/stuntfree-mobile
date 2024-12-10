package com.capstone.c242_ps374.stuntfree.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.capstone.c242_ps374.stuntfree.data.api.ApiService
import com.capstone.c242_ps374.stuntfree.data.manager.SessionManager
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import com.capstone.c242_ps374.stuntfree.data.auth.LoginRequest
import com.capstone.c242_ps374.stuntfree.data.auth.LoginResponse
import com.capstone.c242_ps374.stuntfree.data.auth.RegisterRequest
import com.capstone.c242_ps374.stuntfree.data.auth.RegisterResponse
import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    val isLoggedIn: LiveData<Boolean> = sessionManager.isLoggedIn().asLiveData()

    suspend fun logout() {
        sessionManager.clearSession()
    }

    suspend fun registerUser(registerData: RegisterRequest): Resource<RegisterResponse> {
        return try {
            // Mengambil response dari API
            val response = apiService.registerUser(registerData)

            // Mengecek apakah response sukses
            if (response.isSuccessful) {
                response.body()?.let {
                    // Jika sukses, kembalikan resource sukses
                    Resource.Success(it)
                } ?: Resource.Error("Response body is null")
            } else {
                // Jika response tidak sukses, kembalikan resource error
                Resource.Error("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            // Jika terjadi kesalahan, kembalikan resource error
            Resource.Error("Request failed: ${e.message}")
        }
    }


    suspend fun loginUser(loginData: LoginRequest): Resource<LoginResponse> {
        return try {
            val result = safeApiCall { apiService.loginUser(loginData) }

            when (result) {
                is Resource.Success -> {
                    result.data?.token?.let { token ->
                        sessionManager.saveAuthToken(
                            tokenType = token.tokenType,
                            accessToken = token.accessToken,
                            refreshToken = token.refreshToken
                        )
                    }
                    result
                }

                is Resource.Error -> {
                    result
                }

                is Resource.Loading -> {
                    result
                }
            }
        } catch (e: Exception) {
            Resource.Error("Login failed: ${e.localizedMessage}")
        }
    }


    private suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): Resource<T> {
        return try {
            val response = apiCall()
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        Resource.Success(it)
                    } ?: Resource.Error("Empty response body")
                }
                response.code() == 401 -> {
                    sessionManager.clearSession()
                    Resource.Error("Unauthorized access. Please login again.")
                }
                response.code() == 403 -> {
                    Resource.Error("Access forbidden. Please check your credentials.")
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        Gson().fromJson(errorBody, ErrorResponse::class.java).message
                    } catch (e: Exception) {
                        response.message() ?: "Unknown error occurred"
                    }
                    Resource.Error("API error: $errorMessage")
                }
            }
        } catch (e: IOException) {
            Resource.Error("Network error: Please check your internet connection")
        } catch (e: Exception) {
            Resource.Error("Unexpected error: ${e.localizedMessage}")
        }
    }

    data class ErrorResponse(
        val message: String,
        val status: Boolean
    )
}