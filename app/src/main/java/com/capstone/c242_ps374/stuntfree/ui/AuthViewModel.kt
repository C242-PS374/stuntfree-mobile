package com.capstone.c242_ps374.stuntfree.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.c242_ps374.stuntfree.data.manager.SessionManager
import com.capstone.c242_ps374.stuntfree.data.repository.AuthRepository
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import com.capstone.c242_ps374.stuntfree.data.api.auth.LoginRequest
import com.capstone.c242_ps374.stuntfree.data.api.auth.LoginResponse
import com.capstone.c242_ps374.stuntfree.data.api.auth.RegisterRequest
import com.capstone.c242_ps374.stuntfree.data.api.auth.RegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginStatus = MutableLiveData<Resource<LoginResponse>>()
    val loginStatus: LiveData<Resource<LoginResponse>> get() = _loginStatus

    private val _authState = MutableLiveData<Resource<String?>>()
    val authState: LiveData<Resource<String?>> = _authState

    private val _registrationStatus = MutableLiveData<Resource<RegisterResponse>>()
    val registrationStatus: LiveData<Resource<RegisterResponse>> get() = _registrationStatus

    private val _navigateToQuiz = MutableLiveData<Unit>()
    val navigateToQuiz: LiveData<Unit> get() = _navigateToQuiz

    private val _navigateToMain = MutableLiveData<Unit>()
    val navigateToMain: LiveData<Unit> get() = _navigateToMain

    val isLoggedIn: LiveData<Boolean> = repository.isLoggedIn

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            try {
                val refreshToken = sessionManager.getRefreshToken().first()
                val accessToken = sessionManager.getAccessToken().first()
                val tokenType = sessionManager.getTokenType().first()

                if (!accessToken.isNullOrEmpty() && !tokenType.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                    val fullToken = "$tokenType $accessToken $refreshToken"
                    Log.d("AuthViewModel", "Token Used: $fullToken")
                    _authState.postValue(Resource.Success(fullToken))
                } else {
                    _authState.postValue(Resource.Error("Token or Token Type not found"))
                }
            } catch (e: Exception) {
                _authState.postValue(Resource.Error("Error retrieving token: ${e.message}"))
            }
        }
    }

    private fun checkUserStage(email: String) {
        viewModelScope.launch {
            try {
                val token = sessionManager.getAccessToken().first()
                if (token.isNullOrEmpty()) {
                    _authState.postValue(Resource.Error("Token not found"))
                } else {
                    val stage = sessionManager.getStage(email).first()
                    Log.d("AuthViewModel", "Stage: $stage")
                    if (stage.isNullOrEmpty()) {
                        _navigateToQuiz.postValue(Unit)
                    } else {
                        _navigateToMain.postValue(Unit)
                    }
                }
            } catch (e: Exception) {
                _authState.postValue(Resource.Error("Error retrieving stage or token: ${e.message}"))
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginStatus.value = Resource.Loading()

            try {
                val loginData = LoginRequest(email, password)

                when (val response = repository.loginUser(loginData)) {
                    is Resource.Success -> {
                        Log.d("AuthViewModel", "Login successful: $loginData")
                        val token = response.data?.token
                        Log.d("AuthViewModel", "Token received: $token")


                        if (token != null) {
                            sessionManager.saveAuthToken(token.tokenType, token.accessToken, token.refreshToken)
                            sessionManager.saveEmail(email)

                            sessionManager.getAccessToken().collect { savedToken ->
                                if (!savedToken.isNullOrEmpty()) {
                                    checkUserStage(email)
                                } else {
                                    _loginStatus.value = Resource.Error("Token gagal disimpan.")
                                }
                            }
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
    
    fun registerUser(email: String, name: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _registrationStatus.value = Resource.Loading()

            try {
                val registerData = RegisterRequest(email, name, password, confirmPassword)

                when (val response = repository.registerUser(registerData)) {
                    is Resource.Success -> {
                        response.data?.let { responseData ->
                            if (responseData.error) {
                                _registrationStatus.value = Resource.Error(responseData.message)
                            } else {
                                _registrationStatus.value = Resource.Success(responseData)
                                Log.d("AuthViewModel", "Register User: ${registerData.name} ${registerData.email} ${registerData.password} ${registerData.confirmPassword}")
                            }
                        } ?: run {
                            _registrationStatus.value = Resource.Error("Unknown error: Empty response")
                        }
                    }
                    is Resource.Error -> {
                        _registrationStatus.value = Resource.Error(response.message ?: "Register failed")
                    }
                    else -> {
                        _registrationStatus.value = Resource.Error("Unexpected response")
                    }
                }
            } catch (e: Exception) {
                _registrationStatus.value = Resource.Error("Register failed: ${e.localizedMessage}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            sessionManager.clearSession()
        }
    }
}