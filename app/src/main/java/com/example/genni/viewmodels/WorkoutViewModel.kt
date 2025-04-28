package com.example.genni.viewmodels

import android.content.Context
import android.content.res.Resources
import android.util.Log
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class WorkoutViewModel : ViewModel() {

    private val _allWorkouts = mutableStateListOf<Workout>()
    val allWorkouts: List<Workout> get() = _allWorkouts

    private val _workouts = mutableStateListOf<Workout>()
    val workouts: List<Workout> get() = _workouts

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    var currentGeneratedWorkout by mutableStateOf<List<Workout>>(emptyList())
    var savedWorkouts by mutableStateOf<List<SavedWorkout>>(emptyList())
        private set

    var currentExerciseIndex = mutableStateOf(0)
    var currentSet = mutableStateOf(1)
    var currentState = mutableStateOf(WorkoutState.Exercise)
    var timeLeft = mutableStateOf(0)

    init {
        fetchWorkoutsFromFirestore()
    }

    private fun fetchWorkoutsFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Workouts")
            .get()
            .addOnSuccessListener { result ->
                _allWorkouts.clear()
                for (document in result) {
                    val dto = document.toObject(WorkoutDTO::class.java)
                    _allWorkouts.add(
                        Workout(
                            index = 0,
                            name = dto.name,
                            muscleGroupWorked = dto.muscleGroupWorked,
                            sets = dto.sets,
                            reps = dto.reps,
                            restTime = dto.restTime,
                            imageResID = getImageResId(dto.imageName),
                            equipmentUsed = dto.equipmentUsed
                        )
                    )
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Failed to fetch workouts", exception)
            }
    }

    private fun getImageResId(imageName: String): Int {
        return when (imageName.lowercase()) {
            //TODO: Fill in all the others later
            "plank" -> R.drawable.plank
            "mountainclimbers" -> R.drawable.mountainclimbers
            "russiantwists" -> R.drawable.russiantwists
            "burpees" -> R.drawable.burpees
            "jumpsquats" -> R.drawable.jumpsquats
            "highknees" -> R.drawable.highknees
            "tricepdips" -> R.drawable.dips
            "bicepcurls" -> R.drawable.bicepcurls
            "shouldertaps" -> R.drawable.shouldertaps
            "supermanhold" -> R.drawable.supermanhold
            else -> R.drawable.na
        }
    }

    private fun filterExercisesBy(muscles: List<String>, equipment: List<String>): List<Workout> {
        return _allWorkouts.filter { workout ->
            val matchMuscle = workout.muscleGroupWorked.any { it.trim().equalsAnyIgnoreCase(muscles) }
            val matchEquip = equipment.isEmpty() || workout.equipmentUsed.any { it.trim().equalsAnyIgnoreCase(equipment) }
            matchMuscle && matchEquip
        }.ifEmpty { _allWorkouts.shuffled() }
    }

    private fun String.equalsAnyIgnoreCase(list: List<String>): Boolean {
        return list.any { it.equals(this, ignoreCase = true) }
    }

    fun generateWorkouts(muscles: List<String>, sets: Int, reps: Int, equipment: List<String>, duration: Int) {
        viewModelScope.launch {
            _workouts.clear()
            val filtered = filterExercisesBy(muscles, equipment)
            val count = duration / 5
            val selected = filtered.shuffled().take(count)

            val generatedList = mutableListOf<Workout>() // Create a new list
            selected.forEachIndexed { index, workout ->
                generatedList.add( // Add to the new list
                    workout.copy(
                        index = index + 1,
                        sets = sets.takeIf { it > 0 } ?: Random.nextInt(3, 5),
                        reps = reps.takeIf { it > 0 } ?: Random.nextInt(8, 15),
                        restTime = Random.nextInt(1, 3)
                    )
                )
            }
            _workouts.addAll(generatedList) // Update the _workouts list
            currentGeneratedWorkout = generatedList // Update currentGeneratedWorkout
        }
    }

    fun startWorkout(onFinished: () -> Unit) {
        if (currentExerciseIndex.value >= workouts.size) {
            onFinished()
            return
        }

        val currentWorkout = workouts[currentExerciseIndex.value]
        timeLeft.value = currentWorkout.restTime * 60

        viewModelScope.launch {
            while (currentExerciseIndex.value < workouts.size) {
                when (currentState.value) {
                    WorkoutState.Exercise -> {
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
                        currentState.value = WorkoutState.Exercise
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
        val workout = workouts.getOrNull(currentExerciseIndex.value)
        if (workout != null) {
            if (currentSet.value >= workout.sets) {
                currentSet.value = 1
                currentExerciseIndex.value++
                currentState.value = WorkoutState.Rest
            } else {
                currentSet.value++
                timeLeft.value = 60
                currentState.value = WorkoutState.Exercise
            }
        }
    }

    fun skipExercise() {
        currentExerciseIndex.value++
        currentSet.value = 1
        currentState.value = WorkoutState.Exercise
    }

    // Save the current generated workout list as a SavedWorkout under root "SavedWorkouts"
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

        val timestamp = System.currentTimeMillis()
        val workoutData = workoutsToSave.map { it.toMap() }

        val doc = mapOf(
            "name" to workoutName,
            "timestamp" to timestamp,
            "workouts" to workoutData
        )

        val formattedWorkoutName = workoutName.trim().replace(Regex("[^A-Za-z0-9]"), "_").lowercase()

        if (userId.isNotBlank()) {
            db.collection("Users")
                .document(userId)
                .collection("SavedWorkouts")
                .document(formattedWorkoutName)
                .set(doc)
                .addOnSuccessListener {
                    val saved = SavedWorkout(id = formattedWorkoutName, name = workoutName, timestamp = timestamp, workouts = workoutsToSave)
                    savedWorkouts = listOf(saved) + savedWorkouts
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    onError(e.message ?: "Failed to save workout")
                }
        } else {
            onError("User ID is missing")
        }
    }


    fun loadSavedWorkouts(userId: String, onError: (String) -> Unit = {}) {
        if (userId.isBlank()) {
            onError("User ID is missing")
            return
        }

        db.collection("Users")
            .document(userId)
            .collection("SavedWorkouts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snap ->
                val list = snap.documents.mapNotNull { doc ->
                    val name      = doc.getString("name") ?: return@mapNotNull null
                    val ts        = doc.getLong("timestamp") ?: 0L
                    val rawList   = doc.get("workouts") as? List<Map<String, Any>> ?: return@mapNotNull null
                    val workouts  = rawList.map { map -> map.toWorkout() }
                    SavedWorkout(id = doc.id, name = name, timestamp = ts, workouts = workouts)
                }
                savedWorkouts = list
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to load saved workouts")
            }
    }



    fun Map<String, Any>.toWorkout(): Workout {
        return Workout(
            index = (this["index"] as Long).toInt(),
            name = this["name"] as String,
            sets = (this["sets"] as Long).toInt(),
            reps = (this["reps"] as Long).toInt(),
            restTime = (this["restTime"] as Long).toInt(),
            muscleGroupWorked = this["muscleGroupWorked"] as List<String>,
            equipmentUsed = this["equipmentUsed"] as List<String>,
            imageResID = getImageResId(this["imageName"] as String)
        )
    }

    // Helper function to get the string name of a resource ID.
    fun getImageNameFromRes(context: Context, resId: Int): String {
        return try {
            // Attempt to retrieve the entry name (which is the file name without the extension)
            // of the resource ID from the application's resources.
            context.resources.getResourceEntryName(resId)
        } catch (e: Resources.NotFoundException) {
            // If the resource ID is not found, return a default string "unknown_image".
            "unknown_image"
        }
    }


    // Helper to serialize
    fun Workout.toMap(): Map<String,Any> = mapOf(
        "index"              to index,
        "name"               to name,
        "sets"               to sets,
        "reps"               to reps,
        "restTime"           to restTime,
        "muscleGroupWorked"  to muscleGroupWorked,
        "equipmentUsed"      to equipmentUsed,
        // if you want to reconstruct images later, store the original imageName instead of ID
        //"imageName"          to "" /* you’ll need a reverse lookup here, or omit entirely */
    )

    fun setCurrentWorkout(newWorkout: List<Workout>) {
        _workouts.clear()
        _workouts.addAll(newWorkout)
    }

    fun loadWorkout(savedWorkout: SavedWorkout) {
        currentGeneratedWorkout = savedWorkout.workouts
    }

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