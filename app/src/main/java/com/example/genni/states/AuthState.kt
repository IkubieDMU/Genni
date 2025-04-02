package com.example.genni.states

/**
 * Represents different states of the authentication process.
 */
sealed class AuthState {
    data object Idle : AuthState() // Initial state, no action taken yet
    data object Loading : AuthState() // Indicates that authentication is in progress
    data class Success(val message: String) : AuthState() // Success state with a confirmation message
    data class Error(val message: String) : AuthState() // Error state with an error message
}