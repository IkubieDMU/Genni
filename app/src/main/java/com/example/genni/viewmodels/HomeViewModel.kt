package com.example.genni.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.genni.Screens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ViewModel to manage the UI state of the HomeScreen
class HomeViewModel : ViewModel() {

    private val _selectedWorkout = MutableStateFlow<String?>(null)
    val selectedWorkout: StateFlow<String?> = _selectedWorkout

    // Muscle Groups selection
    private val _selectedMuscleGroups = MutableStateFlow<List<String>>(emptyList())
    val selectedMuscleGroups: StateFlow<List<String>> = _selectedMuscleGroups

    // Sets and Reps selection
    private val _sets = MutableStateFlow(0)
    private val _reps = MutableStateFlow(0)
    private val _restTimePerExercise = MutableStateFlow(0)
    private val _restTimePerSet = MutableStateFlow(0)
    val sets: StateFlow<Int> = _sets
    val reps: StateFlow<Int> = _reps
    val restTimePerExercise: StateFlow<Int> = _restTimePerExercise
    val restTimePerSet: StateFlow<Int> = _restTimePerSet

    // Equipment selection
    private val _selectedEquipment = MutableStateFlow<List<String>>(emptyList())
    val selectedEquipment: StateFlow<List<String>> = _selectedEquipment

    // Duration selection
    private val _duration = MutableStateFlow(15) // Default to 15 minutes
    val duration: StateFlow<Int> = _duration

    fun selectWorkout(workout: String, context: Context) {
        _selectedWorkout.value = workout
        Toast.makeText(context, "$workout selected", Toast.LENGTH_SHORT).show()
    }

    fun setMuscleGroups(muscleGroups: List<String>) {
        _selectedMuscleGroups.value = muscleGroups
    }

    fun setSetsAndReps(sets: Int, reps: Int, restTimePerExercise: Int, restTimePerSet: Int) {
        _sets.value = sets
        _reps.value = reps
        _restTimePerExercise.value = restTimePerExercise
        _restTimePerSet.value = restTimePerSet
    }


    fun setEquipment(equipment: List<String>) {
        _selectedEquipment.value = equipment
    }

    fun setDuration(minutes: Int) {
        _duration.value = minutes
    }

    fun generateWorkout(nc: NavController, context: Context, workoutViewModel: WorkoutViewModel) {
        workoutViewModel.generateWorkouts(
            muscles = selectedMuscleGroups.value,
            sets = sets.value,
            reps = reps.value,
            equipment = selectedEquipment.value,
            duration = duration.value
        )

        Toast.makeText(context, "Workout Generated!", Toast.LENGTH_SHORT).show()
        nc.navigate(Screens.GeneratedWorkoutScreen.screen)
    }
}