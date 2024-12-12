package com.capstone.c242_ps374.stuntfree.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLog
import com.capstone.c242_ps374.stuntfree.data.api.journaling.SubmitFoodLogData
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
import okhttp3.RequestBody
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

    fun submitFoodLog(file: File, foodsList: ArrayList<SubmitFoodLog>) {
        viewModelScope.launch {
            val token = sessionManager.getBearerToken().firstOrNull()
            if (token.isNullOrEmpty()) {
                _submitFood.value = Resource.Error("Token tidak ditemukan. Silakan login ulang.")
                return@launch
            }
            Log.d("SubmitFoodViewModel", "Token: $token")

            _submitFood.value = Resource.Loading()

            try {
                val requestBodyFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData("file", file.name, requestBodyFile)

                val requestBodyFoods = Gson().toJson(foodsList)
                val requestBody = requestBodyFoods.toRequestBody("application/json".toMediaTypeOrNull())

                val result = submitFoodRepository.submitFoodLog(token, filePart, requestBody)
                Log.d("SubmitFoodViewModel", "Result: $result")
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response != null) {
                        Log.d("SubmitFoodViewModel", "Response: $response")
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