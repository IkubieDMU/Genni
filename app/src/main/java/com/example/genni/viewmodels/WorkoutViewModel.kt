package com.example.genni.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genni.models.Workout
import kotlinx.coroutines.launch
import kotlin.random.Random

class WorkoutViewModel: ViewModel() {

    // List of available workouts (exercise names)
    private val workoutList = listOf(
        "Push-Ups", "Bench Press", "Incline Dumbbell Press", "Chest Fly", "Dips",
        "Pull-Ups", "Deadlifts", "Bent-Over Rows", "Lat Pulldown", "Seated Cable Rows",
        "Squats", "Lunges", "Leg Press", "Calf Raises", "Hamstring Curls",
        "Shoulder Press", "Lateral Raises", "Front Raises", "Shrugs", "Face Pulls",
        "Bicep Curls", "Triceps Dips", "Hammer Curls", "Overhead Triceps Extension", "Preacher Curls"
    )

    // Mutable list of Workout objects that will hold generated workouts
    private val _workouts = mutableStateListOf<Workout>()

    // Publicly accessible list of workouts (Read-only)
    val workouts: List<Workout> get() = _workouts

    // Automatically generate workouts when ViewModel is created
    init {
        generateWorkouts()
    }

    /**
     * Generates a list of random workouts based on the given exercise count.
     * Each workout will have:
     * - A random exercise name from the workoutList
     * - Random sets (3-6)
     * - Random reps (3-20)
     * - Random rest time (1-5 mins)
     */
    fun generateWorkouts(exerciseNum: Int = 10) {
        viewModelScope.launch {
            _workouts.clear() // Clear previous workouts before generating new ones
            repeat(exerciseNum) { index ->
                val workout = Workout(
                    index + 1, // Assign an index (1-based)
                    workoutList[Random.nextInt(workoutList.size)], // Random workout name
                    Random.nextInt(3, 6), // Random sets (3-6)
                    Random.nextInt(3, 20), // Random reps (3-20)
                    Random.nextInt(1, 5) // Random rest time (1-5 minutes)
                )
                _workouts.add(workout) // Add the new workout to the list
            }
        }
    }
}
