package com.capstone.c242_ps374.stuntfree.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.c242_ps374.stuntfree.data.manager.SessionManager
import com.capstone.c242_ps374.stuntfree.data.repository.AuthRepository
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import com.capstone.c242_ps374.stuntfree.data.auth.LoginRequest
import com.capstone.c242_ps374.stuntfree.data.auth.LoginResponse
import com.capstone.c242_ps374.stuntfree.data.auth.ProfileResponse
import com.capstone.c242_ps374.stuntfree.data.auth.ProfileResult
import com.capstone.c242_ps374.stuntfree.data.auth.RegisterRequest
import com.capstone.c242_ps374.stuntfree.data.auth.RegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _profileData = MutableLiveData<Resource<ProfileResponse>>()
    val profileData: LiveData<Resource<ProfileResponse>> = _profileData

    private val _loginStatus = MutableLiveData<Resource<LoginResponse>>()
    val loginStatus: LiveData<Resource<LoginResponse>> get() = _loginStatus

    private val _authState = MutableLiveData<Resource<String>>()
    val authState: LiveData<Resource<String>> = _authState

    private val _registrationStatus = MutableLiveData<Resource<RegisterResponse>>()
    val registrationStatus: LiveData<Resource<RegisterResponse>> get() = _registrationStatus

    val isLoggedIn: LiveData<Boolean> = repository.isLoggedIn

    init {
        val token = sessionManager.getAuthToken()
        if (token != null && sessionManager.isLoggedIn()) {
            _authState.value = Resource.Success(token)
        } else {
            _authState.value = Resource.Error("Token not found")
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginStatus.value = Resource.Loading()
            try {
                val loginData = LoginRequest(email, password)
                when (val response = repository.loginUser(loginData)) {
                    is Resource.Success -> {
                        val token = response.data?.loginResult?.token
                        if (!token.isNullOrEmpty()) {
                            sessionManager.saveAuthToken(token)
                            _loginStatus.value = response
                        } else {
                            _loginStatus.value = Resource.Error("Invalid login response: Missing token")
                        }
                    }
                    is Resource.Error -> {
                        _loginStatus.value = Resource.Error(response.message ?: "Login failed")
                    }
                    is Resource.Loading -> {
                        _loginStatus.value = Resource.Loading()
                    }
                }
            } catch (e: Exception) {
                _loginStatus.value = Resource.Error("Login failed: ${e.message}")
            }
        }
    }

    fun checkStage(onNavigateToQuiz: () -> Unit, onNavigateToMain: () -> Unit) {
        val stage = sessionManager.getStage()
        if (stage.isNullOrEmpty()) {
            onNavigateToQuiz()
        } else {
            onNavigateToMain()
        }
    }

    fun registerUser(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _registrationStatus.value = Resource.Loading()
            try {
                val registerData = RegisterRequest(name, email, password, confirmPassword)
                when (val response = repository.registerUser(registerData)) {
                    is Resource.Success -> {
                        response.data?.let { responseData ->
                            if (responseData.error) {
                                _registrationStatus.value = Resource.Error(responseData.message)
                            } else {
                                _registrationStatus.value = Resource.Success(responseData)
                            }
                        } ?: run {
                            _registrationStatus.value = Resource.Error("Unknown error: Empty response")
                        }
                    }
                    is Resource.Error -> {
                        _registrationStatus.value = Resource.Error(response.message ?: "Register failed")
                    }
                    is Resource.Loading -> {
                        _registrationStatus.value = Resource.Loading()
                    }
                }
            } catch (e: Exception) {
                _registrationStatus.value = Resource.Error("Register failed: ${e.localizedMessage}")
            }
        }
    }
}