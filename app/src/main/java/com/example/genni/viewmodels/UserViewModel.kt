package com.example.genni.viewmodels

import androidx.lifecycle.ViewModel
import com.example.genni.models.BreathingExercise
import com.example.genni.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel : ViewModel() {
    //Private database variable
    private val db = FirebaseFirestore.getInstance()

    // Add new User object to the db collection
    fun registerUser(user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // Fetch the highest userID and increment it
        db.collection("Users")
            .orderBy("userID", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                val nextUserID = if (result.isEmpty) {
                    1 // If no users exist, start with 1
                } else {
                    val lastUser = result.documents.firstOrNull()
                    val lastUserID = lastUser?.getLong("userID")?.toInt() ?: 0
                    lastUserID + 1
                }

                val newUser = user.copy(userID = nextUserID) // Set the userID dynamically

                // Save the user to the database with the new userID
                db.collection("Users")
                    .document(newUser.username) // Use the username as the document ID
                    .set(newUser)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e.message ?: "Error") }
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error")
            }
    }

    // For Login
    fun authenticateUser(username: String, password: String, callBack: (User?) -> Unit) {
        db.collection("Users")
            .whereEqualTo("username", username) // ✅ check if username is in the Users Collection
            .whereEqualTo("password", password) // ✅ check if password is in the Users Collection
            .get()
            .addOnSuccessListener { result ->
                // If one user in the Firestore query, convert the first result into a User object and pass it to the callback
                val user = result.documents.firstOrNull()?.toObject(User::class.java)
                callBack(user)
            }
            .addOnFailureListener { callBack(null) }
    }
}