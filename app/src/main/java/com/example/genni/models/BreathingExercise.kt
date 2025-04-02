package com.example.genni.models

// Data model for a breathing exercise
data class BreathingExercise(
    val inhaleTime: Int,  // Time for inhaling (in seconds)
    val holdTime: Int,    // Time for holding breath (in seconds)
    val exhaleTime: Int   // Time for exhaling (in seconds)
)
