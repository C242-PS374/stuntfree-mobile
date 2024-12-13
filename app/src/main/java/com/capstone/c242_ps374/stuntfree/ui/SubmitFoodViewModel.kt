package com.capstone.c242_ps374.stuntfree.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLog
import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLogResponse
import com.capstone.c242_ps374.stuntfree.data.manager.SessionManager
import com.capstone.c242_ps374.stuntfree.data.repository.SubmitFoodRepository
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SubmitFoodViewModel @Inject constructor(
    private val submitFoodRepository: SubmitFoodRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _submitFood = MutableLiveData<Resource<SubmitFoodLogResponse>>()
    val submitFood: LiveData<Resource<SubmitFoodLogResponse>> get() = _submitFood

    fun submitFoodLog(file: File, foodsList: List<SubmitFoodLog>) {
        Log.d("SubmitFoodLog", "Submitting data: $foodsList with image file: ${file.absolutePath}")

        viewModelScope.launch {
            val token = sessionManager.getBearerToken().firstOrNull()
            if (token.isNullOrEmpty()) {
                _submitFood.value = Resource.Error("Token tidak ditemukan. Silakan login ulang.")
                return@launch
            }
            Log.d("SubmitFoodViewModel", "Token: $token")

            Log.d("SubmitFoodViewModel", "File details:")
            Log.d("SubmitFoodViewModel", "Name: ${file.name}")
            Log.d("SubmitFoodViewModel", "Size: ${file.length()} bytes")
            Log.d("SubmitFoodViewModel", "Path: ${file.absolutePath}")

            _submitFood.value = Resource.Loading()

            try {
                // Membuat Multipart untuk file
                val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData(
                    "file", file.name, requestBody
                )

                // Mengonversi foodsList menjadi JSON dan mengirimnya sebagai request body
                val foodsJson = Gson().toJson(foodsList)
                Log.d("SubmitFoodViewModel", "Foods JSON: $foodsJson")
                val foodsRequestBody = foodsJson.toRequestBody("application/json".toMediaTypeOrNull())

                // Memanggil repository untuk mengirim data
                val result = submitFoodRepository.submitFoodLog(token, filePart, foodsRequestBody)
                Log.d("SubmitFoodViewModel", "Result: $result")
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response != null) {
                        Log.d("SubmitFoodViewModel", "Full Response: $response")
                        Log.d("SubmitFoodViewModel", "Message: ${response.message}")
                        _submitFood.value = Resource.Success(response)
                        Log.d("SubmitFoodViewModel", "Food log berhasil dikirim: ${response.message}")
                    } else {
                        Log.d("SubmitFoodViewModel", "Response kosong atau tidak valid")
                        _submitFood.value = Resource.Error("Respons tidak berisi data yang valid.")
                    }
                } else {
                    val errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Terjadi kesalahan."
                    _submitFood.value = Resource.Error(errorMessage)
                }
            } catch (e: Exception) {
                _submitFood.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }
}
