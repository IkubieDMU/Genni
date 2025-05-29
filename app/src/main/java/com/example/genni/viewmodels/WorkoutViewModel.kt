package com.example.genni.viewmodels

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genni.R
import com.example.genni.models.SavedWorkout
import com.example.genni.models.Workout
import com.example.genni.models.WorkoutDTO
import com.example.genni.states.WorkoutState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random


class WorkoutViewModel : ViewModel() {

    // MutableStateListOf for holding all workouts fetched from Firestore.
    // This list is observed for UI updates.
    private val _allWorkouts = mutableStateListOf<Workout>()
    // Publicly exposed immutable list of all workouts.
    val allWorkouts: List<Workout> get() = _allWorkouts

    // MutableStateListOf for holding the currently generated or selected workouts for a session.
    private val _workouts = mutableStateListOf<Workout>()
    // Publicly exposed immutable list of current workouts.
    val workouts: List<Workout> get() = _workouts

    // Firestore instance for database operations.
    private val db = FirebaseFirestore.getInstance()
    // FirebaseAuth instance for user authentication.
    //private val auth = FirebaseAuth.getInstance()

    // MutableState for tracking the progress of the current workout session (e.g., for a progress bar).
    private val _progress = mutableStateOf(0f)
    // Publicly exposed State for observing workout progress.
    val progress: State<Float> = _progress

    // MutableState to hold the list of workouts that were last generated.
    // This is used for saving the workout.
    var currentGeneratedWorkout by mutableStateOf<List<Workout>>(emptyList())
    // MutableStateFlow to hold a list of workouts saved by the user.
    private val _savedWorkouts = MutableStateFlow<List<SavedWorkout>>(emptyList())
    // Publicly exposed StateFlow for observing saved workouts.
    val savedWorkouts: StateFlow<List<SavedWorkout>> = _savedWorkouts.asStateFlow()

    // MutableState for the index of the current exercise in the workout.
    var currentExerciseIndex = mutableStateOf(0)
    // MutableState for the current set number of the current exercise.
    var currentSet = mutableStateOf(1)
    // MutableState for the current state of the workout (e.g., Exercise, Rest, Completed).
    var currentState = mutableStateOf(WorkoutState.Exercise)
    // MutableState for the time left in the current exercise or rest period.
    var timeLeft = mutableStateOf(0)
    // MutableState to track if the workout is paused.
    private val _isPaused = mutableStateOf(false)
    // Publicly exposed boolean for checking if the workout is paused.
    val isPaused: Boolean get() = _isPaused.value

    // Job to manage the coroutine for the workout timer.
    private var workoutJob: Job? = null
    // MutableState to indicate if a workout is currently running.
    var isWorkoutRunning by mutableStateOf(false)

    // Initializer block: fetches all available workouts from Firestore when the ViewModel is created.
    init {
        fetchWorkoutsFromFirestore()
    }

    /**
     * Fetches all workout data from the "Workouts" collection in Firestore.
     * Maps the fetched documents to WorkoutDTO objects and then converts them to Workout models.
     * Updates the `_allWorkouts` list.
     */
    private fun fetchWorkoutsFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Workouts")
            .get()
            .addOnSuccessListener { result ->
                // Clear existing workouts before adding new ones.
                _allWorkouts.clear()
                for (document in result) {
                    // Convert Firestore document to WorkoutDTO.
                    val dto = document.toObject(WorkoutDTO::class.java)
                    // Add the converted Workout to the list.
                    _allWorkouts.add(
                        Workout(
                            index = 0, // Index will be set when generating a specific workout.
                            name = dto.name,
                            muscleGroupWorked = dto.muscleGroupWorked,
                            sets = dto.sets,
                            reps = dto.reps,
                            restTime = dto.restTime,
                            imageResID = getImageResId(dto.imageName), // Get drawable resource ID from image name.
                            equipmentUsed = dto.equipmentUsed
                        )
                    )
                }
            }
            .addOnFailureListener { exception -> Log.e("Firestore", "Failed to fetch workouts", exception) }
    }

    /**
     * Maps an image name string to its corresponding Android drawable resource ID.
     * This allows displaying images based on names stored in Firestore.
     */
    private fun getImageResId(imageName: String): Int {
        return when (imageName.lowercase()) {
            "bbshoulderpress" -> R.drawable.bbshoulderpress
            "benchpress" -> R.drawable.benchpress
            "bicepcurls" -> R.drawable.bicepcurls
            "burpees" -> R.drawable.burpees
            "calfraises" -> R.drawable.calfraises
            "chestfly" -> R.drawable.chestfly
            "squats" -> R.drawable.squats
            "deadlift" -> R.drawable.deadlift
            "dips" -> R.drawable.dips
            "hamstringcurls" -> R.drawable.hamstringcurls
            "highknees" -> R.drawable.highknees
            "ibpress" -> R.drawable.ibpress
            "jumpsquats" -> R.drawable.jumpsquats
            "pulldowns" -> R.drawable.pulldowns
            "legpress" -> R.drawable.legpress
            "lunges" -> R.drawable.lunges
            "mountainclimbers" -> R.drawable.mountainclimbers
            "plank" -> R.drawable.plank
            "pullups" -> R.drawable.pullups
            "pushups" -> R.drawable.pushups
            "russiantwists" -> R.drawable.russiantwists
            "cablerows" -> R.drawable.cablerows
            "shouldertaps" -> R.drawable.shouldertaps
            "supermanhold" -> R.drawable.supermanhold
            else -> R.drawable.na // Default image if no match is found.
        }
    }

    /**
     * Filters the `_allWorkouts` list based on specified muscle groups and equipment.
     * Returns a filtered list. If no workouts match the criteria, it returns a shuffled list of all workouts.
     */
    private fun filterExercisesBy(muscles: List<String>, equipment: List<String>): List<Workout> {
        return _allWorkouts.filter { workout ->
            // Check if any of the workout's muscle groups match the provided muscle list (case-insensitive).
            val matchMuscle = workout.muscleGroupWorked.any { it.trim().equalsAnyIgnoreCase(muscles) }
            // Check if equipment list is empty (no filter) or if any of the workout's equipment matches.
            val matchEquip = equipment.isEmpty() || workout.equipmentUsed.any { it.trim().equalsAnyIgnoreCase(equipment) }
            matchMuscle && matchEquip
        }.ifEmpty {
            // If no workouts match the filters, return a shuffled list of all workouts.
            _allWorkouts.shuffled()
        }
    }

    /**
     * Extension function to check if a String exists in a list of Strings, ignoring case.
     */
    private fun String.equalsAnyIgnoreCase(list: List<String>): Boolean {
        return list.any { it.equals(this, ignoreCase = true) }
    }

    /**
     * Generates a workout plan based on user-selected criteria (muscles, sets, reps, equipment, duration).
     * Filters available exercises, shuffles them, and selects a number of workouts to fit the duration.
     * Randomizes sets, reps, and rest times if not provided.
     */
    fun generateWorkouts(muscles: List<String>, sets: Int, reps: Int, equipment: List<String>, duration: Int) {
        viewModelScope.launch {
            _workouts.clear() // Clear any previously generated workouts.
            val filtered = filterExercisesBy(muscles, equipment) // Filter exercises.

            // Calculate approximate time per set and per workout to determine how many workouts can fit the duration.
            val timePerSet = 1 // Assuming 1 minute per set for calculation purposes.
            val timePerWorkout = timePerSet * sets
            val workoutCount = duration / timePerWorkout
            // Select a random subset of filtered workouts based on the calculated count.
            val selected = filtered.shuffled().take(workoutCount)
            val generatedList = mutableListOf<Workout>()
            selected.forEachIndexed { index, workout ->
                // Create a new Workout object with updated index, sets, reps, and rest time.
                generatedList.add(
                    workout.copy(
                        index = index + 1,
                        sets = sets.takeIf { it > 0 } ?: Random.nextInt(3, 5), // Use provided sets or random between 3-5.
                        reps = reps.takeIf { it > 0 } ?: Random.nextInt(8, 15), // Use provided reps or random between 8-15.
                        restTime = Random.nextInt(1, 3) // Random rest time between 1-3 minutes.
                    )
                )
            }
            _workouts.addAll(generatedList) // Add generated workouts to the current workout list.
            currentGeneratedWorkout = generatedList // Store the generated list for saving.
        }
    }

    /**
     * Suspends the coroutine while the workout is paused.
     * This is called repeatedly with a small delay to efficiently wait.
     */
    private suspend fun waitWhilePaused() {
        while (_isPaused.value) {
            delay(100) // Small delay to prevent busy-waiting and consume CPU.
        }
    }

    /**
     * Starts the workout timer and manages the workout flow (exercises, sets, rest times).
     * Uses a coroutine to handle timing and state changes.
     * Calls `onFinished` when the entire workout is completed.
     */
    fun startWorkout(onFinished: () -> Unit) {
        // Prevent starting a new workout if one is already active.
        if (workoutJob?.isActive == true) return
        isWorkoutRunning = true // Set flag to true as workout begins.
        updateProgress() // Initial progress update.

        workoutJob = viewModelScope.launch {
            // Loop through each exercise in the workout list.
            while (currentExerciseIndex.value < workouts.size) {
                val currentWorkout = workouts[currentExerciseIndex.value]
                // Loop through each set of the current exercise.
                while (currentSet.value <= currentWorkout.sets) {
                    currentState.value = WorkoutState.Exercise // Set state to Exercise.
                    timeLeft.value = 60 // Set exercise time to 60 seconds (1 minute).
                    // Countdown for the exercise.
                    while (timeLeft.value > 0) {
                        delay(1000) // Wait for 1 second.
                        if (!_isPaused.value) {
                            timeLeft.value-- // Decrement time if not paused.
                        } else {
                            waitWhilePaused() // Wait if paused.
                        }
                    }
                    // If all sets for the current exercise are completed, break the inner loop.
                    if (currentSet.value >= currentWorkout.sets) {
                        break
                    } else {
                        currentState.value = WorkoutState.Rest // Set state to Rest.
                        timeLeft.value = currentWorkout.restTime * 60 // Set rest time in seconds.
                        // Countdown for the rest period.
                        while (timeLeft.value > 0) {
                            delay(1000)
                            if (!_isPaused.value) {
                                timeLeft.value--
                            } else {
                                waitWhilePaused()
                            }
                        }
                        currentSet.value++ // Increment set number.
                    }
                }
                currentSet.value = 1 // Reset set for the next exercise.
                currentExerciseIndex.value++ // Move to the next exercise.

                // If there are more exercises, add a rest period between exercises.
                if (currentExerciseIndex.value < workouts.size) {
                    currentState.value = WorkoutState.Rest // Set state to Rest.
                    timeLeft.value = currentWorkout.restTime * 60 // Use the rest time of the just completed workout.
                    while (timeLeft.value > 0) {
                        delay(1000)
                        if (!_isPaused.value) {
                            timeLeft.value--
                        } else {
                            waitWhilePaused()
                        }
                    }
                }
                updateProgress() // Update progress after each exercise is completed.
            }
            currentState.value = WorkoutState.Completed // Set state to Completed when all exercises are done.
            isWorkoutRunning = false // Set flag to false as workout ends.
            onFinished() // Call the callback function.
        }
    }

    /**
     * Updates the workout progress based on completed sets and total sets.
     * The progress is a float between 0f and 1f.
     */
    private fun updateProgress() {
        val totalSets = workouts.sumOf { it.sets } // Calculate total sets across all workouts.
        // Calculate completed sets: sum of sets from completed exercises + (current set - 1) of the current exercise.
        val completedSets = workouts.take(currentExerciseIndex.value).sumOf { it.sets } + (currentSet.value - 1)
        // Update the progress value, ensuring it's between 0f and 1f.
        _progress.value = if (totalSets == 0) 0f else (completedSets.toFloat() / totalSets).coerceIn(0f, 1f)
    }

    /**
     * Skips the current set of the current exercise.
     * If all sets of the current exercise are skipped, it moves to the next exercise.
     * Updates workout state and progress.
     */
    fun skipSet() {
        val workout = workouts.getOrNull(currentExerciseIndex.value)
        if (workout != null) {
            if (currentSet.value >= workout.sets) {
                // If current set is the last set, move to the next exercise.
                currentSet.value = 1
                currentExerciseIndex.value++
                if (currentExerciseIndex.value < workouts.size) {
                    currentState.value = WorkoutState.Exercise
                    timeLeft.value = 60
                } else {
                    currentState.value = WorkoutState.Completed
                    timeLeft.value = 0
                }
            } else {
                // Otherwise, move to the next set of the current exercise.
                currentSet.value++
                currentState.value = WorkoutState.Exercise
                timeLeft.value = 60
            }
            updateProgress() // Update progress after skipping.
        }
    }

    /**
     * Skips the current exercise and moves to the next one.
     * Resets the set count for the new exercise.
     * Updates workout state and progress.
     */
    fun skipExercise() {
        currentExerciseIndex.value++ // Increment exercise index.
        currentSet.value = 1 // Reset set count for the new exercise.
        if (currentExerciseIndex.value < workouts.size) {
            currentState.value = WorkoutState.Exercise
            timeLeft.value = 60
        } else {
            currentState.value = WorkoutState.Completed
            timeLeft.value = 0
        }
        updateProgress() // Update progress after skipping.
    }

    /**
     * Pauses the workout by setting the `_isPaused` flag to true.
     */
    fun pauseWorkout() {
        _isPaused.value = true
    }

    /**
     * Unpauses the workout by setting the `_isPaused` flag to false.
     */
    fun unpauseWorkout() {
        _isPaused.value = false
    }

    /**
     * Saves the `currentGeneratedWorkout` to Firestore under the user's saved workouts.
     * Takes workout name, user ID, context, and success/error callbacks.
     * Formats the workout name for document ID.
     */
    fun saveCurrentWorkout(workoutName: String, userId: String, context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (workoutName.isBlank()) {
            onError("Workout name cannot be empty.")
            return
        }

        val workoutsToSave = currentGeneratedWorkout
        if (workoutsToSave.isEmpty()) {
            onError("No generated workout to save.")
            return
        }

        val timestamp = System.currentTimeMillis() // Get current timestamp for saving.
        val workoutData = workoutsToSave.map { it.toMap() } // Convert list of Workouts to list of Maps.

        val doc = mapOf(
            "name" to workoutName,
            "timestamp" to timestamp,
            "workouts" to workoutData // Store workouts as a list of maps.
        )

        // Format workout name to be a valid Firestore document ID.
        val formattedWorkoutName = workoutName.trim().replace(Regex("[^A-Za-z0-9]"), "_").lowercase()

        if (userId.isNotBlank()) {
            db.collection("Users")
                .document(userId)
                .collection("SavedWorkouts")
                .document(formattedWorkoutName) // Use formatted name as document ID.
                .set(doc)
                .addOnSuccessListener {
                    // Create a SavedWorkout object and add it to the beginning of the savedWorkouts list.
                    val saved = SavedWorkout(id = formattedWorkoutName, name = workoutName, timestamp = timestamp, workouts = workoutsToSave)
                    _savedWorkouts.value = listOf(saved) + _savedWorkouts.value
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    onError(e.message ?: "Failed to save workout")
                }
        } else {
            onError("User ID is missing")
        }
    }

    /**
     * Extension function to convert a Map<String, Any> (from Firestore) back into a Workout object.
     * Handles type casting and potential errors.
     */
    fun Map<String, Any>.toWorkout(): Workout? {
        return try {
            Workout(
                index = (this["index"] as? Long)?.toInt() ?: -1,
                name = this["name"] as? String ?: "",
                sets = (this["sets"] as? Long)?.toInt() ?: 0,
                reps = (this["reps"] as? Long)?.toInt() ?: 0,
                restTime = (this["restTime"] as? Long)?.toInt() ?: 0,
                muscleGroupWorked = this["muscleGroupWorked"] as? List<String> ?: emptyList(),
                equipmentUsed = this["equipmentUsed"] as? List<String> ?: emptyList(),
                imageResID = (this["imageName"] as? String)?.let { getImageResId(it) } ?: 0, // Convert image name back to resource ID.
                progress = (this["progress"] as? Double)?.toFloat() ?: 0f
            )
        } catch (e: ClassCastException) {
            Log.e("Workout Conversion", "Error converting map to Workout: ${e.message}", e)
            null // Return null if conversion fails due to ClassCastException.
        }
    }

    /**
     * Retrieves the resource entry name from a drawable resource ID.
     */
    fun getImageNameFromRes(context: Context, resId: Int): String {
        return try {
            context.resources.getResourceEntryName(resId)
        } catch (e: Resources.NotFoundException) {
            "unknown_image"
        }
    }

    /**
     * Extension function to convert a Workout object into a Map<String, Any> for Firestore storage.
     * Excludes `imageResID` and `progress` as they are derived/computed values.
     */
    fun Workout.toMap(): Map<String,Any> = mapOf(
        "index"              to index,
        "name"               to name,
        "sets"               to sets,
        "reps"               to reps,
        "restTime"           to restTime,
        "muscleGroupWorked"  to muscleGroupWorked,
        "equipmentUsed"      to equipmentUsed,
    )

    /**
     * Loads saved workouts for a specific user from Firestore.
     * Orders the workouts by timestamp in descending order.
     * Updates the `_savedWorkouts` StateFlow.
     */
    fun loadSavedWorkouts(userId: String, onError: (String) -> Unit = {}) {
        if (userId.isBlank()) {
            onError("User ID is missing")
            return
        }
        Log.d("LoadWorkouts", "Loading saved workouts for user: $userId")

        db.collection("Users")
            .document(userId)
            .collection("SavedWorkouts")
            .orderBy("timestamp", Query.Direction.DESCENDING) // Order by timestamp for most recent first.
            .get()
            .addOnSuccessListener { snap ->
                // Map Firestore documents to SavedWorkout objects.
                val list = snap.documents.mapNotNull { doc ->
                    val name = doc.getString("name") ?: return@mapNotNull null
                    val ts = doc.getLong("timestamp") ?: 0L
                    val rawWorkouts = doc.get("workouts") as? List<Map<String, Any>>
                    if (rawWorkouts != null) {
                        // Convert the raw list of maps back to a list of Workout objects.
                        val workouts = rawWorkouts.mapNotNull { it.toWorkout() }
                        SavedWorkout(id = doc.id, name = name, timestamp = ts, workouts = workouts)
                    } else {
                        Log.w("LoadWorkouts", "Skipping document ${doc.id} - 'workouts' field is missing or not a List")
                        null
                    }
                }
                _savedWorkouts.value = list // Update the StateFlow's value.
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to load saved workouts")
            }
    }

    /**
     * Sets the current workout list to a new provided list of workouts.
     * Clears existing workouts and adds the new ones.
     */
    fun setCurrentWorkout(newWorkout: List<Workout>) {
        _workouts.clear()
        _workouts.addAll(newWorkout)
    }

    /**
     * Loads a `SavedWorkout` into the `currentGeneratedWorkout` for viewing or restarting.
     */
    fun loadWorkout(savedWorkout: SavedWorkout) {
        currentGeneratedWorkout = savedWorkout.workouts
    }

    /**
     * Deletes a saved workout from Firestore for a specific user.
     * Takes the workout ID and user ID, along with success/error callbacks.
     */
    fun deleteSavedWorkout(workoutId: String, userId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (userId.isBlank()) {
            onError("User ID is missing")
            return
        }

        db.collection("Users")
            .document(userId)
            .collection("SavedWorkouts")
            .document(workoutId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Failed to delete workout") }
    }
}