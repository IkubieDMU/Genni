package com.example.genni.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgetPasswordViewModel : ViewModel() {
    // Private variables
    var email by mutableStateOf("")
        private set
    private val _uiState = MutableStateFlow<ResetState>(ResetState.Idle)
    val uiState: StateFlow<ResetState> = _uiState

    // METHODS

    // Updates email
    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    // Reset original password
    fun resetPassword() {
        if (email.isBlank()) {
            _uiState.value = ResetState.Error("Email cannot be empty")
            return
        }
        _uiState.value = ResetState.Loading
        viewModelScope.launch {
            delay(5000) // Simulating network call --> Delaying 5 Seconds
            _uiState.value = ResetState.Success("Password reset link sent to $email")
        }
    }
}

// UI state for Forgot Password
sealed class ResetState {
    object Idle : ResetState()
    object Loading : ResetState()
    data class Success(val message: String) : ResetState()
    data class Error(val message: String) : ResetState()
}