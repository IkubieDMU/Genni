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
    private val db = FirebaseFirestore.getInstance()

    fun registerUser(user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        db.collection("Users")
            .orderBy("userID", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                val nextUserID = if (result.isEmpty) {
                    1
                } else {
                    val lastUser = result.documents.firstOrNull()
                    val lastUserID = lastUser?.getLong("userID")?.toInt() ?: 0
                    lastUserID + 1
                }
                val newUser = user.copy(userID = nextUserID)

                db.collection("Users")
                    .document(newUser.username)
                    .set(newUser)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e.message ?: "Error") }
            }
            .addOnFailureListener { e -> onFailure(e.message ?: "Error") }
    }

    fun authenticateUser(username: String, password: String, callBack: (User?) -> Unit) {
        db.collection("Users")
            .whereEqualTo("username", username)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { result ->
                val user = result.documents.firstOrNull()?.toObject(User::class.java)
                callBack(user)
            }
            .addOnFailureListener { callBack(null) }
    }
}
