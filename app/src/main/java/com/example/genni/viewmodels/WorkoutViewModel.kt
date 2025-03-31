package com.example.genni.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genni.R
import com.example.genni.models.Workout
import com.example.genni.states.WorkoutState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class WorkoutViewModel : ViewModel() {

    // Mutable list that holds the generated workouts
    private val _workouts = mutableStateListOf<Workout>()

    // Publicly accessible list of workouts (read-only)
    val workouts: List<Workout> get() = _workouts

    // Holds the index of the current exercise being performed (Using MutableState for Compose recomposition)
    var currentExerciseIndex = mutableStateOf(0)

    // Holds the current set number of the ongoing exercise (Using MutableState for Compose recomposition)
    var currentSet = mutableStateOf(1)

    // Tracks the current state of the workout - Exercise, Rest, or Completed (Using MutableState for Compose recomposition)
    var currentState = mutableStateOf(WorkoutState.Exercise)

    // Time remaining for an exercise or rest period (in seconds) (Using MutableState for Compose recomposition)
    var timeLeft = mutableStateOf(0)

    // List of exercises from which workouts will be randomly generated
    private val workoutList = listOf(
        "Push-Ups", "Bench Press", "Incline Barbell Press", "Chest Fly", "Dips",
        "Pull-Ups", "Deadlifts", "BB Shoulder Press", "Lat Pulldowns", "Seated Cable Rows",
        "DB/BB Squats", "Lunges", "Leg Press", "Calf Raises", "Hamstring Curls",
    )

    private val workoutImages = listOf(
        R.drawable.pushups,
        R.drawable.benchpress,
        R.drawable.ibpress,
        R.drawable.chestfly,
        R.drawable.dips,
        R.drawable.pullups,
        R.drawable.deadlift,
        R.drawable.bbshoulderpress,
        R.drawable.pulldowns,
        R.drawable.cablerows,
        R.drawable.squats,
        R.drawable.lunges,
        R.drawable.legpress,
        R.drawable.calfraises,
        R.drawable.hamstringcurls,
    )

    init {
        if (_workouts.isEmpty()) {  // Ensure workouts are only generated once
            generateWorkouts(3)
        }
    }

    /**
     * Generates a random list of workouts based on the specified exercise count.
     * Each workout has randomly generated sets, reps, and rest time.
     */
    fun generateWorkouts(exerciseNum: Int) {
        if (_workouts.isNotEmpty()) return // Prevent regenerating workouts if they already exist

        viewModelScope.launch {
            _workouts.clear() // Clear previous workouts before generating new ones
            val shuffledExercises = workoutList.zip(workoutImages).shuffled()
            val selectedExercises = shuffledExercises.take(exerciseNum)

            selectedExercises.forEachIndexed { index, (exerciseName, imageResID) ->
                val setNum = Random.nextInt(3, 6)
                val repNum = Random.nextInt(3, 15)
                val restTimeNum = Random.nextInt(1, 5)

                val workout = Workout(
                    index = index + 1,
                    name = exerciseName,
                    sets = setNum,
                    reps = repNum,
                    restTime = restTimeNum,
                    imageResID = imageResID
                )
                _workouts.add(workout)
            }
        }
    }

    /**
     * Starts the workout session, iterating through each workout and managing exercise and rest periods.
     * Automatically transitions to the next set or workout after completion.
     */
    fun startWorkout(workouts: List<Workout>, onFinished: () -> Unit) {
        if (currentExerciseIndex.value >= workouts.size) {
            onFinished() // If all exercises are completed, call the completion callback
            return
        }

        // Get the current workout based on the index
        val currentWorkout = workouts[currentExerciseIndex.value]

        // Initialize the timer based on the workout's rest time (in seconds)
        timeLeft.value = currentWorkout.restTime * 60

        viewModelScope.launch {
            while (currentExerciseIndex.value < workouts.size) {
                when (currentState.value) {
                    WorkoutState.Exercise -> { // When performing an exercise
                        timeLeft.value = 60 // Set a default exercise duration of 60 seconds
                        while (timeLeft.value > 0) {
                            delay(1000) // Wait for 1 second before decreasing the timer
                            timeLeft.value--
                        }

                        if (currentSet.value >= currentWorkout.sets) { // If all sets are completed
                            currentSet.value = 1 // Reset set counter
                            currentExerciseIndex.value++ // Move to the next exercise
                            currentState.value = WorkoutState.Rest // Transition to Rest state
                        } else {
                            currentSet.value++ // Proceed to the next set
                        }
                    }

                    WorkoutState.Rest -> { // When resting between exercises
                        while (timeLeft.value > 0) {
                            delay(1000) // Wait for 1 second before decreasing the timer
                            timeLeft.value--
                        }
                        currentState.value =
                            WorkoutState.Exercise // Switch back to exercise state
                    }

                    WorkoutState.Completed -> { // When the workout session is completed
                        onFinished() // Trigger the callback to indicate workout completion
                        return@launch
                    }
                }
            }
        }
    }
    /**
     * Skips the current set and moves to the rest state.
     */
    fun skipSet() {
        val currentWorkout = workouts.getOrNull(currentExerciseIndex.value)
        if (currentWorkout != null) {
            if (currentSet.value >= currentWorkout.sets) {
                // If all sets for the current exercise are done, move to the next exercise
                currentSet.value = 1
                currentExerciseIndex.value++
                currentState.value = WorkoutState.Rest
            } else {
                // Move to the next set and reset the timer
                currentSet.value++
                timeLeft.value = 60 // Resetting timer to 60 seconds for the next set
                currentState.value = WorkoutState.Exercise
            }
        }
    }

    /**
     * Skips the current exercise and moves to the next one.
     */
    fun skipExercise() {
        currentExerciseIndex.value++ // Move to the next exercise
        currentSet.value = 1 // Reset the set counter for the next exercise
        currentState.value = WorkoutState.Exercise // Start exercising immediately
    }
}
