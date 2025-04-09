package com.example.genni.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.genni.models.Admin
import com.example.genni.states.AuthState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminViewModel: ViewModel() {
    // Private DB Variable
    private val db = FirebaseFirestore.getInstance()

    // Username & Password Variables
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    // Variable for storing current Logged in Admin
    private val _currentAdmin = MutableStateFlow<Admin?>(null)
    val admin: StateFlow<Admin?> = _currentAdmin

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // Methods
    fun onAdminUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onAdminPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun registerAdmin(admin: Admin, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        db.collection("Admins").orderBy("adminID", Query.Direction.DESCENDING).limit(1).get()
            .addOnSuccessListener { result ->
                val nextAdminID = if (result.isEmpty) {
                    1 // If no users exist, start with 1
                } else {
                    val lastAdmin = result.documents.firstOrNull()
                    val lastAdminID = lastAdmin?.getLong("adminID")?.toInt() ?: 0
                    lastAdminID + 1
                }
                val newAdmin = admin.copy(adminID = nextAdminID) // Set the userID dynamically

                // Save the user to the database with the new userID
                db.collection("Admins").document(newAdmin.username)/*Use the username as the document ID*/.set(newAdmin)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e.message ?: "Error") }
            }
    }

    fun checkAdminInDB(username: String, password: String, callBack: (Admin?) -> Unit) {
        db.collection("Admins")
            .whereEqualTo("username", username) // ✅ check if username is in the Admin Collection
            .whereEqualTo("password", password) // ✅ check if password is in the Admin Collection
            .get()
            .addOnSuccessListener { result ->
                // If one user in the Firestore query, convert the first result into a User object and pass it to the callback
                val admin = result.documents.firstOrNull()?.toObject(Admin::class.java) // Stores the response as an Admin Object
                callBack(admin)
            }
            .addOnFailureListener { callBack(null) }
    }

    fun authenticateAdmin(username: String, password: String, onLoginSuccess: () -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Fields cannot be empty")
            return
        } // If fields are blank, return Error message
        checkAdminInDB(username, password) { admin ->
            if (admin != null) {
                _currentAdmin.value = admin // Save logged-in admin
                _authState.value = AuthState.Success("Login successful!")
                onLoginSuccess()
            } else {
                _authState.value = AuthState.Error("Invalid Username or Password")
            }
        }
    }
}