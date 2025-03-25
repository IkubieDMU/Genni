package com.example.genni.models

// Workout Data Model
data class Workout(
    val index: Int,
    val name: String,
    val sets: Int,
    val reps: Int,
    val restTime: Int
)