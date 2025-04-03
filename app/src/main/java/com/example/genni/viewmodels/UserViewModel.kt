package com.example.genni.viewmodels

import androidx.lifecycle.ViewModel
import com.example.genni.models.BreathingExercise
import com.example.genni.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel : ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private var userIdCounter = 1 // Auto-incrementing userID

    fun registerUser(user: User) {
        val updatedList = _users.value.toMutableList()
        updatedList.add(user)
        _users.value = updatedList
    }

    fun authenticateUser(username: String, password: String): User? {
        return _users.value.find { it.username == username && it.password == password }
    }
}
