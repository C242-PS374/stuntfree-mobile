package com.capstone.c242_ps374.stuntfree.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ScanFoodViewModel @Inject constructor(
    private val scanFoodRepository: ScanFoodRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _foodScanResult = MutableLiveData<Resource<FoodScanResponse>>()
    val foodScanResult: LiveData<Resource<FoodScanResponse>> get() = _foodScanResult

    fun scanFood(imageFile: File) {
        viewModelScope.launch {
            _foodScanResult.value = Resource.Loading()

            // Ambil token sekali di awal
            val token = sessionManager.getBearerToken().firstOrNull()
            if (token.isNullOrEmpty()) {
                _foodScanResult.value = Resource.Error("Token tidak ditemukan. Silakan login ulang.")
                return@launch
            }

            Log.d("ScanFoodViewModel", "File details:")
            Log.d("ScanFoodViewModel", "Name: ${imageFile.name}")
            Log.d("ScanFoodViewModel", "Size: ${imageFile.length()} bytes")
            Log.d("ScanFoodViewModel", "Path: ${imageFile.absolutePath}")

            // Periksa ukuran file
            val fileToProcess = if (imageFile.length() > 10 * 1024 * 1024) {
                compressImageFile(imageFile) ?: run {
                    _foodScanResult.value = Resource.Error("Gagal mengompresi file.")
                    return@launch
                }
            } else {
                imageFile
            }

            // Proses upload
            uploadImage(fileToProcess, token)
        }
    }

    private fun compressImageFile(originalFile: File): File? {
        return try {
            val bitmap = BitmapFactory.decodeFile(originalFile.absolutePath)
            val compressedFile = File(originalFile.parent, "compressed_${originalFile.name}")

            FileOutputStream(compressedFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out)
            }
            compressedFile
        } catch (e: Exception) {
            Log.e("ScanFoodViewModel", "Compression error: ${e.message}", e)
            null
        }
    }

    private suspend fun uploadImage(file: File, token: String) {
        try {
            val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)

            val result = scanFoodRepository.scanFood(token, filePart)

            if (result.isSuccess) {
                result.getOrNull()?.let {
                    Log.d("ScanFoodViewModel", "Full Response: $result")
                    _foodScanResult.value = Resource.Success(it)
                } ?: run {
                    _foodScanResult.value = Resource.Error("Respons kosong atau tidak valid")
                }
            } else {
                _foodScanResult.value = Resource.Error(result.exceptionOrNull()?.message ?: "Terjadi kesalahan")
            }
        } catch (e: Exception) {
            Log.e("ScanFoodViewModel", "Upload error: ${e.message}", e)
            _foodScanResult.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }
}