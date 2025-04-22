package com.example.genni.viewmodels

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genni.R
import com.example.genni.models.Workout
import com.example.genni.models.WorkoutDTO
import com.example.genni.states.WorkoutState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class WorkoutViewModel : ViewModel() {

    private val _allWorkouts = mutableStateListOf<Workout>()
    val allWorkouts: List<Workout> get() = _allWorkouts

    private val _workouts = mutableStateListOf<Workout>()
    val workouts: List<Workout> get() = _workouts

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

            selected.forEachIndexed { index, workout ->
                _workouts.add(
                    workout.copy(
                        index = index + 1,
                        sets = sets.takeIf { it > 0 } ?: Random.nextInt(3, 5),
                        reps = reps.takeIf { it > 0 } ?: Random.nextInt(8, 15),
                        restTime = Random.nextInt(1, 3)
                    )
                )
            }
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

    val savedWorkouts = mutableStateListOf<Pair<String, List<Workout>>>()

    fun loadSavedWorkouts(userId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").document(userId).collection("SavedWorkouts")
            .get()
            .addOnSuccessListener { result ->
                savedWorkouts.clear()
                for (doc in result) {
                    val name = doc.getString("name") ?: "Unnamed"
                    val exercises = (doc["exercises"] as? List<Map<String, Any>>)?.map { it.toWorkout() } ?: emptyList()
                    savedWorkouts.add(name to exercises)
                }
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


    fun saveCurrentWorkout(context: Context, name: String, userId: String, onResult: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val workoutMap = mapOf(
            "name" to name,
            "timestamp" to System.currentTimeMillis(),
            "exercises" to workouts.map { it.toMap(context) }
        )

        db.collection("Users")
            .document(userId)
            .collection("SavedWorkouts")
            .add(workoutMap)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getImageNameFromRes(context: Context, resId: Int): String {
        return try {
            context.resources.getResourceEntryName(resId)
        } catch (e: Resources.NotFoundException) {
            "unknown_image"
        }
    }


    // Helper to serialize
    fun Workout.toMap(context: Context): Map<String, Any> {
        return mapOf(
            "index" to index,
            "name" to name,
            "sets" to sets,
            "reps" to reps,
            "restTime" to restTime,
            "muscleGroupWorked" to muscleGroupWorked,
            "equipmentUsed" to equipmentUsed,
            "imageName" to getImageNameFromRes(context, imageResID)
        )
    }

    fun setCurrentWorkout(newWorkout: List<Workout>) {
        _workouts.clear()
        _workouts.addAll(newWorkout)
    }
}