package com.example.genni.models

import java.util.Date

// User Data Model
data class User(
    val userID: Int,
    val firstName: String,
    val middleName: String = "",
    val lastName: String,
    val age: Int,
    val gender: String,
    val goals: List<String>,
    val yearsOfTraining: Int = 0,
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val profilePic: String? = null,
    val foodRecommendations: List<String>,
    val weight: Double,
    val height: Double
) {
    // No-argument constructor for Firebase deserialization
    constructor() : this(
        userID = 0,
        firstName = "",
        middleName = "",
        lastName = "",
        age = 0,
        gender = "",
        goals = listOf(),
        yearsOfTraining = 0,
        username = "",
        password = "",
        email = "",
        profilePic = null,
        foodRecommendations = listOf(),
        weight = 0.0,
        height = 0.0
    )
}