package com.example.genni.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.genni.models.Workout
import com.example.genni.states.WorkoutState
import com.example.genni.ui.theme.white
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class WorkoutViewModel : ViewModel() {

    private val _workouts = mutableStateListOf<Workout>()
    val workouts: List<Workout> get() = _workouts

    var currentExerciseIndex = mutableStateOf(0)
    var currentSet = mutableStateOf(1)
    var currentState = mutableStateOf(WorkoutState.Exerise)
    var timeLeft = mutableStateOf(0)

    private val workoutList = listOf(
        "Push-Ups", "Bench Press", "Incline Dumbbell Press", "Chest Fly", "Dips",
        "Pull-Ups", "Deadlifts", "Bent-Over Rows", "Lat Pulldown", "Seated Cable Rows",
        "Squats", "Lunges", "Leg Press", "Calf Raises", "Hamstring Curls",
        "Shoulder Press", "Lateral Raises", "Front Raises", "Shrugs", "Face Pulls",
        "Bicep Curls", "Triceps Dips", "Hammer Curls", "Overhead Triceps Extension", "Preacher Curls",
        "Plank", "Mountain Climbers", "Crunches", "Russian Twists", "Leg Raises",
        "Burpees", "Jump Squats", "High Knees", "Jumping Jacks", "Step-Ups",
        "Farmer's Walk", "Renegade Rows", "Goblet Squat", "Overhead Press", "Chest Press"
    )

    init {
        generateWorkouts()
    }

    fun generateWorkouts(exerciseNum: Int = 10) {
        viewModelScope.launch {
            _workouts.clear()
            repeat(exerciseNum) { index ->
                val workout = Workout(
                    index + 1,
                    workoutList[Random.nextInt(workoutList.size)],
                    Random.nextInt(3, 6),
                    Random.nextInt(3, 20),
                    Random.nextInt(1, 5)
                )
                _workouts.add(workout)
            }
        }
    }

    fun startWorkout(workouts: List<Workout>, onFinished: () -> Unit) {
        if (currentExerciseIndex.value >= workouts.size) {
            onFinished()
            return
        }

        val currentWorkout = workouts[currentExerciseIndex.value]
        timeLeft.value = currentWorkout.restTime * 60

        viewModelScope.launch {
            while (currentExerciseIndex.value < workouts.size) {
                when (currentState.value) {
                    WorkoutState.Exerise -> {
                        timeLeft.value = 60
                        while (timeLeft.value > 0) {
                            delay(1000)
                            timeLeft.value--
                        }

                        if (currentSet.value >= currentWorkout.sets) {
                            currentSet.value = 1
                            currentExerciseIndex.value++
                            currentState.value = WorkoutState.Rest
                        } else {
                            currentSet.value++
                        }
                    }

                    WorkoutState.Rest -> {
                        while (timeLeft.value > 0) {
                            delay(1000)
                            timeLeft.value--
                        }
                        currentState.value = WorkoutState.Exerise
                    }

                    WorkoutState.Completed -> {
                        onFinished()
                        return@launch
                    }
                }
            }
        }
    }

    fun skipSet() {
        currentState.value = WorkoutState.Rest
    }

    fun skipExercise() {
        currentExerciseIndex.value++
        currentSet.value = 1
        currentState.value = WorkoutState.Exerise
    }
}

