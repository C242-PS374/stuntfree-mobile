package com.capstone.c242_ps374.stuntfree.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.c242_ps374.stuntfree.data.attach.InfancyRequest
import com.capstone.c242_ps374.stuntfree.data.attach.InfancyResponse
import com.capstone.c242_ps374.stuntfree.data.attach.PregnancyRequest
import com.capstone.c242_ps374.stuntfree.data.attach.PregnancyResponse
import com.capstone.c242_ps374.stuntfree.data.attach.UserProfileRequest
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

    private val _stageLiveData = MutableLiveData<String>()
    val stageLiveData: LiveData<String> get() = _stageLiveData

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

    fun attachInfancyProfile(body: InfancyRequest) {
        handleApiRequest(_infancyProfileResponse) {
            attachRepository.attachInfancyProfile(body)
        }
    }

    fun attachPregnancyProfile(body: PregnancyRequest) {
        handleApiRequest(_pregnancyProfileResponse) {
            attachRepository.attachPregnancyProfile(body)
        }
    }

    fun getUserProfile() {
        handleApiRequest(_userProfileResponse) {
            attachRepository.getProfile()
        }
    }

    private fun <T> handleApiRequest(
        liveData: MutableLiveData<Resource<T>>,
        apiCall: suspend () -> Result<T>
    ) {
        liveData.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val result = apiCall()
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response != null) {
                        liveData.value = Resource.Success(response)
                    } else {
                        liveData.value = Resource.Error("Data tidak ditemukan.")
                    }
                } else {
                    val errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Terjadi kesalahan."
                    liveData.value = Resource.Error(errorMessage)
                }
            } catch (e: Exception) {
                liveData.value = Resource.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }
}
