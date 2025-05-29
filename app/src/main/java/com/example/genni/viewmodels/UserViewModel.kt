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
    // Initializes a Firestore database instance to interact with the Firebase backend.
    private val db = FirebaseFirestore.getInstance()

    /**
     * Registers a new user in Firestore.
     * Before saving the user, it determines the next available userID by querying the last existing user.
     *
     * @param user The [User] object containing the new user's data (username, password, etc.).
     * @param onSuccess A lambda function to be called if the user registration is successful.
     * @param onFailure A lambda function to be called if the user registration fails,
     * providing an error message.
     */
    fun registerUser(user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // Query the "Users" collection to get the user with the highest userID.
        // This is done to ensure unique and sequential user IDs.
        db.collection("Users")
            .orderBy("userID", Query.Direction.DESCENDING) // Order by userID in descending order.
            .limit(1) // Limit to one result to get the highest userID.
            .get()
            .addOnSuccessListener { result ->
                // Determine the next available userID.
                val nextUserID = if (result.isEmpty) {
                    // If no users exist yet, start with userID 1.
                    1
                } else {
                    // Get the last user document.
                    val lastUser = result.documents.firstOrNull()
                    // Extract the last userID, defaulting to 0 if not found, and increment it.
                    val lastUserID = lastUser?.getLong("userID")?.toInt() ?: 0
                    lastUserID + 1
                }
                // Create a new User object with the determined nextUserID.
                val newUser = user.copy(userID = nextUserID)

                // Save the new user to the "Users" collection, using their username as the document ID.
                db.collection("Users")
                    .document(newUser.username) // Use username as the document ID for easy retrieval.
                    .set(newUser) // Set the user data.
                    .addOnSuccessListener { onSuccess() } // Call onSuccess if saving is successful.
                    .addOnFailureListener { e -> onFailure(e.message ?: "Error") } // Call onFailure with error message.
            }
            .addOnFailureListener { e -> onFailure(e.message ?: "Error") } // Handle failure during the initial userID query.
    }

    /**
     * Authenticates a user by checking their username and password against Firestore records.
     *
     * @param username The username provided by the user.
     * @param password The password provided by the user.
     * @param callBack A lambda function that will be called with the authenticated [User] object
     * if successful, or `null` if authentication fails.
     */
    fun authenticateUser(username: String, password: String, callBack: (User?) -> Unit) {
        // Query the "Users" collection where username and password match.
        db.collection("Users")
            .whereEqualTo("username", username) // Filter by matching username.
            .whereEqualTo("password", password) // Filter by matching password.
            .get()
            .addOnSuccessListener { result ->
                // If a document is found, convert it to a User object.
                val user = result.documents.firstOrNull()?.toObject(User::class.java)
                callBack(user) // Call the callback with the authenticated user or null.
            }
            .addOnFailureListener {
                // If there's an error during the query (e.g., network issue), call callback with null.
                callBack(null)
            }
    }
}
