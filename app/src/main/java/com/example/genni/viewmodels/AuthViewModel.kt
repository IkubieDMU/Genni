package com.example.genni.viewmodels

import androidx.compose.runtime.Composable
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
    // Mutable state for the username input field.
    // 'by mutableStateOf("")' makes it an observable state holder.
    var username by mutableStateOf("")
    // Mutable state for the password input field.
    var password by mutableStateOf("")

    // MutableStateFlow to represent the authentication state (Idle, Loading, Success, Error).
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    // Publicly exposed StateFlow for observing the authentication state from the UI.
    val authState: StateFlow<AuthState> = _authState

    // MutableStateFlow to store the currently logged-in user object.
    // It emits 'null' if no user is logged in.
    private val _currentUser = MutableStateFlow<User?>(null)
    // Publicly exposed StateFlow for observing the current user.
    val currentUser: StateFlow<User?> = _currentUser

    /**
     * Updates the [username] state whenever the username input field changes.
     * @param newUsername The new username string from the input field.
     */
    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    /**
     * Updates the [password] state whenever the password input field changes.
     * @param newPassword The new password string from the input field.
     */
    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    /**
     * Authenticates the user using the provided username and password.
     * It delegates the actual authentication logic to [UserViewModel].
     * Updates [_authState] based on the authentication result and triggers a callback on success.
     * @param userViewModel An instance of UserViewModel to handle user authentication.
     * @param onLoginSuccess A callback function to be executed if login is successful.
     */
    fun authenticateUser(userViewModel: UserViewModel, onLoginSuccess: () -> Unit) {
        // Check if either username or password fields are empty.
        if (username.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Fields cannot be empty") // Set error state.
            return // Stop execution.
        }

        // Delegate to UserViewModel for authentication.
        // The lambda receives the authenticated User object (or null if authentication fails).
        userViewModel.authenticateUser(username, password) { user ->
            if (user != null) {
                _currentUser.value = user // If authentication is successful, save the logged-in user.
                _authState.value = AuthState.Success("Login successful!") // Set success state.
                onLoginSuccess() // Trigger the success callback.
            } else {
                _authState.value = AuthState.Error("Invalid Username or Password") // Set error state for invalid credentials.
            }
        }
    }

    /**
     * Logs out the current user.
     * Clears the current user, username, and password states.
     * Resets the authentication state to idle and triggers a logout callback.
     * @param onLogout A callback function to be executed after logout is complete.
     */
    fun logout(onLogout: () -> Unit) {
        _currentUser.value = null // Clear the current user.
        username = "" // Clear the username field.
        password = "" // Clear the password field.
        _authState.value = AuthState.Idle // Reset authentication state.
        onLogout() // Trigger the logout callback.
    }
}