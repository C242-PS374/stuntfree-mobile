package com.capstone.c242_ps374.stuntfree.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.c242_ps374.stuntfree.data.attach.InfancyRequest
import com.capstone.c242_ps374.stuntfree.data.attach.InfancyResponse
import com.capstone.c242_ps374.stuntfree.data.attach.PregnancyRequest
import com.capstone.c242_ps374.stuntfree.data.attach.PregnancyResponse
import com.capstone.c242_ps374.stuntfree.data.attach.UserProfileResponse
import com.capstone.c242_ps374.stuntfree.data.manager.SessionManager
import com.capstone.c242_ps374.stuntfree.data.repository.AttachRepository
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttachViewModel @Inject constructor(
    private val attachRepository: AttachRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _infancyProfileResponse = MutableLiveData<Resource<InfancyResponse>>()
    val infancyProfileResponse: LiveData<Resource<InfancyResponse>> get() = _infancyProfileResponse

    private val _pregnancyProfileResponse = MutableLiveData<Resource<PregnancyResponse>>()
    val pregnancyProfileResponse: LiveData<Resource<PregnancyResponse>> get() = _pregnancyProfileResponse

    private val _userProfileResponse = MutableLiveData<Resource<UserProfileResponse>>()
    val userProfileResponse: LiveData<Resource<UserProfileResponse>> get() = _userProfileResponse

    private val _stageLiveData = MutableLiveData<String>()
    val stageLiveData: LiveData<String> get() = _stageLiveData

    // Menyimpan stage ketika disubmit
    fun onSubmit(isPregnancySelected: Boolean, isInfancySelected: Boolean) {
        viewModelScope.launch {
            val stage = when {
                isPregnancySelected -> "pregnancy"
                isInfancySelected -> "infancy"
                else -> "no_selection"
            }

            sessionManager.saveStage(stage)
            _stageLiveData.postValue(stage)
        }
    }

    // Menyertakan profil infancy ke server
    fun attachInfancyProfile(body: InfancyRequest) {
        viewModelScope.launch {
            val token = sessionManager.getBearerToken().firstOrNull()
            if (token.isNullOrEmpty()) {
                _infancyProfileResponse.value = Resource.Error("Token tidak ditemukan. Silakan login ulang.")
                return@launch
            }

            _infancyProfileResponse.value = Resource.Loading() // Menampilkan loading indicator
            try {
                val result = attachRepository.attachInfancyProfile(token, body)
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response != null) {
                        _infancyProfileResponse.value = Resource.Success(response)
                        Log.d("AttachViewModel", "Infancy profile berhasil ditambahkan: ${response.message}")
                    } else {
                        _infancyProfileResponse.value = Resource.Error("Data infancy tidak ditemukan.")
                    }
                } else {
                    val errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Terjadi kesalahan."
                    _infancyProfileResponse.value = Resource.Error(errorMessage)
                }
            } catch (e: Exception) {
                _infancyProfileResponse.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }


    // Menyertakan profil pregnancy ke server
    fun attachPregnancyProfile(body: PregnancyRequest) {
        viewModelScope.launch {
            val token = sessionManager.getBearerToken().firstOrNull()
            if (token.isNullOrEmpty()) {
                _pregnancyProfileResponse.value = Resource.Error("Token tidak ditemukan. Silakan login ulang.")
                return@launch
            }

            _pregnancyProfileResponse.value = Resource.Loading() // Menampilkan loading indicator
            try {
                val result = attachRepository.attachPregnancyProfile(token, body)
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response != null) {
                        _pregnancyProfileResponse.value = Resource.Success(response)
                        Log.d("AttachViewModel", "Pregnancy profile berhasil ditambahkan: ${response.message}")
                    } else {
                        _pregnancyProfileResponse.value = Resource.Error("Data pregnancy tidak ditemukan.")
                    }
                } else {
                    _pregnancyProfileResponse.value = Resource.Error("Gagal menambahkan profil pregnancy.")
                }
            } catch (e: Exception) {
                _pregnancyProfileResponse.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }

    // Mengambil profil pengguna
    fun getUserProfile() {
        viewModelScope.launch {
            val token = sessionManager.getBearerToken().firstOrNull()
            if (token.isNullOrEmpty()) {
                _userProfileResponse.value = Resource.Error("Token tidak ditemukan. Silakan login ulang.")
                return@launch
            }

            _userProfileResponse.value = Resource.Loading() // Menampilkan loading indicator
            try {
                val result = attachRepository.getProfile(token)
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response != null) {
                        _userProfileResponse.value = Resource.Success(response)
                        Log.d("AttachViewModel", "Profil pengguna berhasil diambil")
                    } else {
                        _userProfileResponse.value = Resource.Error("Profil pengguna tidak ditemukan.")
                    }
                } else {
                    _userProfileResponse.value = Resource.Error("Gagal mengambil profil pengguna.")
                }
            } catch (e: Exception) {
                _userProfileResponse.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }
}
