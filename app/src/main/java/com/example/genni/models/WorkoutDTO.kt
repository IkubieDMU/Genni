package com.example.genni.models

data class WorkoutDTO(
    val name: String = "",
    val muscleGroupWorked: List<String> = emptyList(),
    val sets: Int = 0,
    val reps: Int = 0,
    val restTime: Int = 0,
    val imageName: String = "",
    val equipmentUsed: List<String> = emptyList()
)
