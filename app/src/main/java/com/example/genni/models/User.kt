package com.example.genni.models

import android.media.Image

// User Data Model
data class User(
    val userID: Int,
    val firstName: String,
    val middleName: String = "", //Optional
    val lastName: String,
    val age: Int, // Limit it to 105 yrs
    val gender: Char, // i.e. M or F
    val goal: List<String>, // List of all the user's goals
    val yearsOfTraining: Int = 0, // 0yrs of training as the default value
    val username: String,
    val password: String,
    val email: String,
    val profilePic: Image? = null, // Optional
    val foodRecommendations: List<String>, // List of food recommendations dependent on the user's goals
    val weight: Double, // in kg
    val height: Double, // in cm
)