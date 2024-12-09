package com.capstone.c242_ps374.stuntfree.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.c242_ps374.stuntfree.data.manager.SessionManager
import com.capstone.c242_ps374.stuntfree.data.repository.AuthRepository
import com.capstone.c242_ps374.stuntfree.ui.utils.Resource
import com.capstone.c242_ps374.stuntfree.data.auth.LoginRequest
import com.capstone.c242_ps374.stuntfree.data.auth.LoginResponse
import com.capstone.c242_ps374.stuntfree.data.auth.RegisterRequest
import com.capstone.c242_ps374.stuntfree.data.auth.RegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
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
            sessionManager.getAccessToken().collect { token ->
                if (!token.isNullOrEmpty()) {
                    _authState.postValue(Resource.Success(token))
                } else {
                    _authState.postValue(Resource.Error("Token not found"))
                }
            }
        }
    }

    private fun checkUserStage() {
        viewModelScope.launch {
            sessionManager.getAccessToken().collect { token ->
                if (token.isNullOrEmpty()) {
                    _authState.postValue(Resource.Error("Token not found"))
                } else {
                    sessionManager.getStage().collect { stage ->
                        if (stage.isNullOrEmpty()) {
                            _navigateToQuiz.postValue(Unit)
                        } else {
                            _navigateToMain.postValue(Unit)
                        }
                    }
                }
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
                        val token = response.data?.token
                        Log.d("AuthViewModel", "Token received: $token")

                        if (token != null) {
                            sessionManager.saveAuthToken(token.accessToken, token.refreshToken)
                            checkUserStage()
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