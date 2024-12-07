package com.capstone.c242_ps374.stuntfree.ui

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

    private val _authState = MutableLiveData<Resource<String?>>()
    val authState: LiveData<Resource<String?>> = _authState

    fun attachInfancyProfile(body: InfancyRequest) {
        _infancyProfileResponse.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val result = attachRepository.attachInfancyProfile(body)
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response != null) {
                        _infancyProfileResponse.value = Resource.Success(response)
                    } else {
                        _infancyProfileResponse.value = Resource.Error("Data tidak ditemukan.")
                    }
                } else {
                    _infancyProfileResponse.value = Resource.Error(result.exceptionOrNull()?.localizedMessage ?: "Unknown error")
                }
            } catch (e: Exception) {
                _infancyProfileResponse.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }

    fun attachPregnancyProfile(request: PregnancyRequest) {
        _pregnancyProfileResponse.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val result = attachRepository.attachPregnancyProfile(request)
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response != null) {
                        _pregnancyProfileResponse.value = Resource.Success(response)
                    } else {
                        _pregnancyProfileResponse.value = Resource.Error("Data tidak ditemukan.")
                    }
                } else {
                    _pregnancyProfileResponse.value = Resource.Error(result.exceptionOrNull()?.localizedMessage ?: "Unknown error")
                }
            } catch (e: Exception) {
                _pregnancyProfileResponse.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }

    fun getUserProfile() {
        _userProfileResponse.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val result = attachRepository.getProfile()
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response != null) {
                        _userProfileResponse.value = Resource.Success(response)
                    } else {
                        _userProfileResponse.value = Resource.Error("Data profil tidak ditemukan.")
                    }
                } else {
                    _userProfileResponse.value = Resource.Error(result.exceptionOrNull()?.localizedMessage ?: "Unknown error")
                }
            } catch (e: Exception) {
                _userProfileResponse.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authState.value = Resource.Loading()
            try {
                sessionManager.clearSession()
                _authState.value = Resource.Success(null)
            } catch (e: Exception) {
                _authState.value = Resource.Error(e.message ?: "Logout failed")
            }
        }
    }
}