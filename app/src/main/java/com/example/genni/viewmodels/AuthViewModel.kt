package com.example.genni.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genni.states.AuthState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    // Holds the user's username input
    var username by mutableStateOf("")
    private set // Ensures only ViewModel can modify it

    // Holds the user's password input
    var password by mutableStateOf("")
    private set // Ensures only ViewModel can modify it

            // Holds the UI state for authentication process
            private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)

    // Publicly exposed immutable StateFlow so UI can observe changes
    val authState: StateFlow<AuthState> = _authState

    // METHODS

    /**
     * Updates the username state when the user types in the username field.
     * @param newUsername The updated username entered by the user.
     */
    fun onUsernameChange(newUsername: String) {
        username = newUsername  // Update username state
    }

    /**
     * Updates the password state when the user types in the password field.
     * @param newPassword The updated password entered by the user.
     */
    fun onPasswordChange(newPassword: String) {
        password = newPassword  // Update password state
    }

    /**
     * Handles user authentication.
     * It checks if fields are empty, then simulates a login process.
     */
    fun authenticateUser(onLoginSuccess: () -> Unit) {
        // Check if fields are empty, show an error state if true
        if (username.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Fields cannot be empty")
            return
        }

        // Set UI state to Loading to indicate ongoing login process
        _authState.value = AuthState.Loading

        // Simulating network call using coroutine
        viewModelScope.launch {
            delay(5000) // Simulating a delay of 5 seconds for authentication

            // Check if username and password match expected credentials
            if (username == "admin" && password == "admin123") {
                _authState.value = AuthState.Success("Login successful!")
                delay(1000) //Wait 1 Seconds
                onLoginSuccess() // Navigate to next screen
            } else {
                _authState.value = AuthState.Error("Invalid Username or Password")
            }
        }
    }
}


