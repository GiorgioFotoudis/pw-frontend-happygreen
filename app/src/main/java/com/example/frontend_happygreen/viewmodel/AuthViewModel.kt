package com.example.frontend_happygreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_happygreen.data.model.TokenDto
import com.example.frontend_happygreen.data.model.UserProfileDto
import com.example.frontend_happygreen.data.model.LoginRequest
import com.example.frontend_happygreen.data.remote.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val api = ApiClient.service

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _userProfile = MutableStateFlow<UserProfileDto?>(null)
    val userProfile: StateFlow<UserProfileDto?> = _userProfile

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    fun login(username: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.service.login(LoginRequest(username, password))
                _token.value = response.access
                _loginError.value = null
                onSuccess()
            } catch (e: Exception) {
                _loginError.value = "Login fallito: ${e.localizedMessage}"
                onError(e.localizedMessage ?: "Errore")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(username: String, email: String, password: String, password2: String, onSuccess: () -> Unit) {
        _isLoading.value = true
        _loginError.value = null
        viewModelScope.launch {
            runCatching {
                val request = com.example.frontend_happygreen.data.model.RegisterRequest(
                    username = username,
                    email = email,
                    password = password,
                    password2 = password2
                )
                val result = api.register(request)
                _userProfile.value = result
                onSuccess()
            }.onFailure {
                _loginError.value = "Registrazione fallita: ${it.localizedMessage}"
            }.also {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        _token.value = null
        _userProfile.value = null
    }
}