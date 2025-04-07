package com.example.genni.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.genni.models.User
import com.example.genni.states.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // Store the logged-in user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun authenticateUser(userViewModel: UserViewModel, onLoginSuccess: () -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Fields cannot be empty")
            return
        }
        userViewModel.authenticateUser(username, password) { user ->
            if (user != null) {
                _currentUser.value = user // Save logged-in user
                _authState.value = AuthState.Success("Login successful!")
                onLoginSuccess()
            } else {
                _authState.value = AuthState.Error("Invalid Username or Password")
            }
        }
    }
}

