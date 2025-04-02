package com.example.genni.states

/**
 * Represents different states of the password reset process.
 */
sealed class ResetState {
    data object Idle : ResetState() // Initial state, no action taken yet
    data object Loading : ResetState() // Indicates that the password reset is in progress
    data class Success(val message: String) : ResetState() // Success state with a confirmation message
    data class Error(val message: String) : ResetState() // Error state with an error message
}