package com.example.genni.models

data class SavedWorkout(
    val id: String,
    val name: String,
    val timestamp: Long,
    val workouts: List<Workout>
)