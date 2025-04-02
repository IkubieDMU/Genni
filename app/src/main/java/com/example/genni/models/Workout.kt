package com.example.genni.models

// Workout Data Model
data class Workout(
    val index: Int,
    val name: String,
    val sets: Int,
    val reps: Int,
    val restTime: Int,
    val imageResID: Int,
    val progress: Float = 0f // Default value of 0
)