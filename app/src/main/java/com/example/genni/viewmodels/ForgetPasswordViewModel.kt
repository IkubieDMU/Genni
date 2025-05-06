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

    var email by mutableStateOf("")
        private set

    private val _uiState = MutableStateFlow<ResetState>(ResetState.Idle)
    val uiState: StateFlow<ResetState> = _uiState

    private val db = FirebaseFirestore.getInstance()

    private val _navigateToResetPage = MutableStateFlow(false)
    val navigateToResetPage: StateFlow<Boolean> = _navigateToResetPage

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun resetPassword() {
        if (email.isBlank()) {
            _uiState.value = ResetState.Error("Email cannot be empty")
            return
        }

        _uiState.value = ResetState.Loading

        viewModelScope.launch {
            try {
                val querySnapshot = db.collection("Users").whereEqualTo("email", email).get().await()

                if (querySnapshot.isEmpty) {
                    _uiState.value = ResetState.Error("No account found with this email")
                    return@launch
                }

                _navigateToResetPage.value = true
                _uiState.value = ResetState.Idle // Reset state

            } catch (e: Exception) {
                _uiState.value = ResetState.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }

    fun updatePassword(newPassword: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val querySnapshot = db.collection("Users").whereEqualTo("email", email).get().await()

                if (!querySnapshot.isEmpty) {
                    val docId = querySnapshot.documents.first().id
                    db.collection("Users").document(docId).update("password", newPassword).await()
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun onNavigateHandled() {
        _navigateToResetPage.value = false
    }
}