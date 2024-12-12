package com.capstone.c242_ps374.stuntfree.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.c242_ps374.stuntfree.data.api.scan.FoodScanResponse
import com.capstone.c242_ps374.stuntfree.data.manager.SessionManager
import com.capstone.c242_ps374.stuntfree.data.repository.ScanFoodRepository
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ScanFoodViewModel @Inject constructor(
    private val scanFoodRepository: ScanFoodRepository,
    private val sessionManager: SessionManager
): ViewModel() {

    private val _foodScanResult = MutableLiveData<Resource<FoodScanResponse>>()
    val foodScanResult: LiveData<Resource<FoodScanResponse>> get() = _foodScanResult

    fun scanFood(imageFile: File) {
        Log.d("ScanFoodViewModel", "scanFood() called with file: $imageFile")
        viewModelScope.launch {
            val token = sessionManager.getBearerToken().firstOrNull()
            if (token.isNullOrEmpty()) {
                _foodScanResult.value = Resource.Error("Token tidak ditemukan. Silakan login ulang.")
                return@launch
            }

            // Tambahkan pengecekan ukuran file
            if (imageFile.length() > 10 * 1024 * 1024) { // Batasi ukuran file misalnya 10MB
                _foodScanResult.value = Resource.Error("Ukuran file terlalu besar")
                return@launch
            }

            _foodScanResult.value = Resource.Loading()

            try {
                // Pastikan tipe media benar
                val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData(
                    "file",
                    imageFile.name,
                    requestBody
                )

                Log.d("ScanFoodViewModel", "File details:")
                Log.d("ScanFoodViewModel", "Name: ${imageFile.name}")
                Log.d("ScanFoodViewModel", "Size: ${imageFile.length()} bytes")
                Log.d("ScanFoodViewModel", "Path: ${imageFile.absolutePath}")

                val result = scanFoodRepository.scanFood(token, filePart)

                // Logging response details
                Log.d("ScanFoodViewModel", "API Result: $result")

                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response != null) {
                        Log.d("ScanFoodViewModel", "Full Response: $response")
                        Log.d("ScanFoodViewModel", "Message: ${response.message}")
                        Log.d("ScanFoodViewModel", "Result Size: ${response.result.size}")

                        _foodScanResult.value = Resource.Success(response)
                    } else {
                        _foodScanResult.value = Resource.Error("Respons kosong atau tidak valid")
                    }
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Terjadi kesalahan"
                    Log.e("ScanFoodViewModel", "Error: $errorMessage")
                    _foodScanResult.value = Resource.Error(errorMessage)
                }
            } catch (e: Exception) {
                Log.e("ScanFoodViewModel", "Exception: ${e.message}", e)
                _foodScanResult.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }
}