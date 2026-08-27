package com.capstone.c242_ps374.stuntfree.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.c242_ps374.stuntfree.data.api.infancy.InfancyRequest
import com.capstone.c242_ps374.stuntfree.data.api.infancy.InfancyResponse
import com.capstone.c242_ps374.stuntfree.data.api.pregnancy.PregnancyRequest
import com.capstone.c242_ps374.stuntfree.data.api.pregnancy.PregnancyResponse
import com.capstone.c242_ps374.stuntfree.data.api.attach.UserProfileResponse
import com.capstone.c242_ps374.stuntfree.data.api.get.PredictResponse
import com.capstone.c242_ps374.stuntfree.data.api.get.TodayResponse
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

    private val _predictStuntingResponse = MutableLiveData<Resource<PredictResponse>>()
    val predictStuntingResponse: LiveData<Resource<PredictResponse>> get() = _predictStuntingResponse

    private val _todayLogResponse = MutableLiveData<Resource<TodayResponse>>()
    val todayLogResponse: LiveData<Resource<TodayResponse>> get() = _todayLogResponse

    fun onSubmit(isPregnancySelected: Boolean, isInfancySelected: Boolean) {
        viewModelScope.launch {
            val email = sessionManager.getEmail().firstOrNull()
            if (email != null) {
                val stage = when {
                    isPregnancySelected -> "pregnancy"
                    isInfancySelected -> "infancy"
                    else -> "no_selection"
                }

                sessionManager.saveStage(email, stage)
                _stageLiveData.postValue(stage)
                Log.d("AttachViewModel", "Stage for now: $stage")
            }
        }
    }

    fun attachProfile(stage: Stage, body: Any) {
        viewModelScope.launch {
            val token = sessionManager.getBearerToken().firstOrNull()
            if (token.isNullOrEmpty()) {
                when (stage) {
                    Stage.INFANCY -> _infancyProfileResponse.value = Resource.Error("Token tidak ditemukan. Silakan login ulang.")
                    Stage.PREGNANCY -> _pregnancyProfileResponse.value = Resource.Error("Token tidak ditemukan. Silakan login ulang.")
                }
                return@launch
            }

            when (stage) {
                Stage.INFANCY -> {
                    _infancyProfileResponse.value = Resource.Loading()
                    handleInfancyProfile(token, body as InfancyRequest)
                }
                Stage.PREGNANCY -> {
                    _pregnancyProfileResponse.value = Resource.Loading()
                    handlePregnancyProfile(token, body as PregnancyRequest)
                }
            }
        }
    }


    fun getUserProfile() {
        viewModelScope.launch {
            val token = sessionManager.getBearerToken().firstOrNull()

            if (token.isNullOrEmpty()) {
                _userProfileResponse.value = Resource.Error("Token tidak ditemukan. Silakan login ulang.")
                return@launch
            }

            _userProfileResponse.value = Resource.Loading()

            try {
                val result = attachRepository.getProfile(token)

                if (result.isSuccess) {
                    val response = result.getOrNull()

                    if (response != null) {
                        _userProfileResponse.value = Resource.Success(response)
                        Log.d("AttachViewModel", "Name: ${response.data?.profile?.name}, Email: ${response.data?.email}")
                        Log.d("AttachViewModel", "Profil pengguna berhasil diambil")
                    } else {
                        _userProfileResponse.value = Resource.Error("Profil pengguna tidak ditemukan.")
                    }
                } else {
                    val errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Gagal mengambil profil pengguna."
                    _userProfileResponse.value = Resource.Error(errorMessage)
                }
            } catch (e: Exception) {
                _userProfileResponse.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }

    fun predictStunting() {
        viewModelScope.launch {
            val token = sessionManager.getBearerToken().firstOrNull()

            if (token.isNullOrEmpty()) {
                _predictStuntingResponse.value = Resource.Error("Token tidak ditemukan. Silakan login ulang.")
                return@launch
            }

            _predictStuntingResponse.value = Resource.Loading()
            try {
                val result = attachRepository.predictStunting(token)
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response != null) {
                        _predictStuntingResponse.value = Resource.Success(response)
                        Log.d("AttachViewModel", "Stunting berhasil diprediksi: ${response.message}")
                        Log.d("AttachViewModel", "Stunting berhasil diprediksi: ${response.result}")
                    }
                } else {
                    val errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Gagal memprediksi stunting."
                    _predictStuntingResponse.value = Resource.Error(errorMessage)
                }
            } catch (e: Exception) {
                _predictStuntingResponse.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }

    fun todayLog() {
        viewModelScope.launch {
            val token = sessionManager.getBearerToken().firstOrNull()

            if (token.isNullOrEmpty()) {
                _todayLogResponse.value = Resource.Error("Token tidak ditemukan. Silakan login ulang.")
                return@launch
            }

            _todayLogResponse.value = Resource.Loading()
            try {
                val result = attachRepository.todayLog(token)
                Log.d("AttachViewModel", "API Response: $result")
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response != null) {
                        _todayLogResponse.value = Resource.Success(response)
                        Log.d("AttachViewModel", "Log makanan berhasil diambil: ${response.message}")
                        Log.d("AttachViewModel", "Log makanan berhasil diambil: ${response.result}")
                    }
                } else {
                    _todayLogResponse.value = Resource.Error("Gagal mengambil log makanan.")
                }
            } catch (e: Exception) {
                _todayLogResponse.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
                Log.e("AttachViewModel", "Error: ${e.localizedMessage}")
            }
        }
    }

    private suspend fun handleInfancyProfile(token: String, body: InfancyRequest) {
        try {
            val result = attachRepository.attachInfancyProfile(token, body)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response != null) {
                    _infancyProfileResponse.value = Resource.Success(response)
                    Log.d("AttachViewModel", "Infancy profile berhasil ditambahkan: ${response.message}")
                } else {
                    _infancyProfileResponse.value = Resource.Error("Respons tidak berisi data yang valid.")
                }
            } else {
                val errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Terjadi kesalahan."
                _infancyProfileResponse.value = Resource.Error(errorMessage)
            }
        } catch (e: Exception) {
            _infancyProfileResponse.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }

    private suspend fun handlePregnancyProfile(token: String, body: PregnancyRequest) {
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
                val errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Gagal menambahkan profil pregnancy."
                _pregnancyProfileResponse.value = Resource.Error(errorMessage)
            }
        } catch (e: Exception) {
            _pregnancyProfileResponse.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
        }
    }


    enum class Stage(val value: String) {
        INFANCY("infancy"),
        PREGNANCY("pregnancy")
    }

}