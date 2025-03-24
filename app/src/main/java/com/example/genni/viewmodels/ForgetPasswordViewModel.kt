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

// ViewModel to manage the Forgot Password screen logic
class ForgetPasswordViewModel : ViewModel() {

    // Holds the user's email input
    // `mutableStateOf` allows Compose to reactively update UI when this value changes
    var email by mutableStateOf("")
        private set  // `private set` ensures that only the ViewModel can modify the email

    // Holds the UI state for the password reset process
    // `_uiState` is private to ensure controlled updates inside the ViewModel
    private val _uiState = MutableStateFlow<ResetState>(ResetState.Idle)

    // Publicly exposed immutable StateFlow so the UI can observe changes
    val uiState: StateFlow<ResetState> = _uiState

    // METHODS

    /**
     * Updates the email state when the user types in the email field.
     * @param newEmail The updated email entered by the user.
     */
    fun onEmailChange(newEmail: String) {
        email = newEmail  // Update email state
    }

    /**
     * Handles the password reset logic.
     * It checks if the email field is empty, then simulates sending a reset email.
     */
    fun resetPassword() {
        // Check if email is empty, show an error state if true
        if (email.isBlank()) {
            _uiState.value = ResetState.Error("Email cannot be empty")
            return
        }

        // Set UI state to Loading to indicate ongoing process
        _uiState.value = ResetState.Loading

        // Simulating network call using coroutine
        viewModelScope.launch {
            delay(5000) // Simulating a delay of 5 seconds for network request
            // Update UI state to Success when the reset process is completed
            _uiState.value = ResetState.Success("Password reset link sent to $email")
        }
    }
}

/**
 * Represents different states of the password reset process.
 */
sealed class ResetState {
    object Idle : ResetState() // Initial state, no action taken yet
    object Loading : ResetState() // Indicates that the password reset is in progress
    data class Success(val message: String) : ResetState() // Success state with a confirmation message
    data class Error(val message: String) : ResetState() // Error state with an error message
}
