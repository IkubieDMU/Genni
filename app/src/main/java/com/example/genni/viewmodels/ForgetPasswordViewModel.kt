package com.example.genni.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genni.states.ResetState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ViewModel to manage the Forgot Password screen logic
class ForgetPasswordViewModel : ViewModel() {

    // Mutable state for the email input field.
    // 'by mutableStateOf("")' makes it an observable state holder.
    // 'private set' means it can only be changed internally within the ViewModel.
    var email by mutableStateOf("")
        private set

    // MutableStateFlow to represent the UI state of the password reset process.
    // It emits different states like Idle, Loading, or Error.
    private val _uiState = MutableStateFlow<ResetState>(ResetState.Idle)
    // Publicly exposed StateFlow for observing the UI state from the UI layer.
    val uiState: StateFlow<ResetState> = _uiState

    // Firestore instance to interact with the Firebase Firestore database.
    private val db = FirebaseFirestore.getInstance()

    // MutableStateFlow to trigger navigation to the password reset page.
    // It emits 'true' when navigation should occur, and 'false' after it's handled.
    private val _navigateToResetPage = MutableStateFlow(false)
    // Publicly exposed StateFlow for observing the navigation trigger.
    val navigateToResetPage: StateFlow<Boolean> = _navigateToResetPage

    /**
     * Updates the [email] state whenever the email input field changes.
     * @param newEmail The new email string from the input field.
     */
    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    /**
     * Initiates the password reset process.
     * It first validates the email input.
     * Then, it queries Firestore to check if an account exists with the provided email.
     * If an account is found, it sets [_navigateToResetPage] to true to trigger navigation.
     * It updates [_uiState] to reflect loading, error, or idle states.
     */
    fun resetPassword() {
        // Check if the email field is blank. If so, set an error state and return.
        if (email.isBlank()) {
            _uiState.value = ResetState.Error("Email cannot be empty")
            return
        }

        // Set the UI state to Loading to indicate an ongoing operation.
        _uiState.value = ResetState.Loading

        // Launch a coroutine in the ViewModel's scope for asynchronous operations.
        viewModelScope.launch {
            try {
                // Query the "Users" collection in Firestore to find a document where the "email" field matches the provided email.
                // '.await()' is a suspend function that waits for the Firestore operation to complete.
                val querySnapshot = db.collection("Users").whereEqualTo("email", email).get().await()

                // If the query snapshot is empty, no account was found with that email.
                if (querySnapshot.isEmpty) {
                    _uiState.value = ResetState.Error("No account found with this email")
                    return@launch // Exit the coroutine.
                }

                // If an account is found, set the navigation flag to true.
                _navigateToResetPage.value = true
                // Reset the UI state to Idle after successful check, as navigation will handle the next step.
                _uiState.value = ResetState.Idle

            } catch (e: Exception) {
                // Catch any exceptions that occur during the Firestore operation and set an error state.
                _uiState.value = ResetState.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Updates the password for the user associated with the current [email].
     * This function is typically called on the subsequent password reset screen.
     * It queries the user by email, finds their document, and updates the "password" field.
     * @param newPassword The new password string to set.
     * @param onResult A callback function that indicates whether the password update was successful (Boolean).
     */
    fun updatePassword(newPassword: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Query Firestore again to get the user's document based on the email.
                val querySnapshot = db.collection("Users").whereEqualTo("email", email).get().await()

                // If an account is found (querySnapshot is not empty).
                if (!querySnapshot.isEmpty) {
                    // Get the document ID of the first matching user.
                    val docId = querySnapshot.documents.first().id
                    // Update the "password" field for that specific user document.
                    db.collection("Users").document(docId).update("password", newPassword).await()
                    onResult(true) // Indicate success.
                } else {
                    onResult(false) // Indicate failure (e.g., account not found, though it should have been checked earlier).
                }
            } catch (e: Exception) {
                onResult(false) // Indicate failure if an error occurs during the update.
            }
        }
    }

    /**
     * Resets the [navigateToResetPage] flag to `false` after navigation has been handled by the UI.
     * This is crucial to prevent re-triggering navigation on configuration changes or recompositions.
     */
    fun onNavigateHandled() {
        _navigateToResetPage.value = false
    }
}