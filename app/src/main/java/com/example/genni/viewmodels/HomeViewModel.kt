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

    // MutableStateFlow to hold the currently selected workout type (e.g., "Full Body", "Upper Body").
    // It's nullable, indicating no workout is selected initially.
    private val _selectedWorkout = MutableStateFlow<String?>(null)
    // Publicly exposed StateFlow for observing the selected workout from the UI.
    val selectedWorkout: StateFlow<String?> = _selectedWorkout

    // Muscle Groups selection
    // MutableStateFlow to hold a list of selected muscle groups (e.g., ["Chest", "Triceps"]).
    private val _selectedMuscleGroups = MutableStateFlow<List<String>>(emptyList())
    // Publicly exposed StateFlow for observing the selected muscle groups.
    val selectedMuscleGroups: StateFlow<List<String>> = _selectedMuscleGroups

    // Sets and Reps selection
    // MutableStateFlow for the number of sets.
    private val _sets = MutableStateFlow(0)
    // MutableStateFlow for the number of reps.
    private val _reps = MutableStateFlow(0)
    // MutableStateFlow for rest time between exercises.
    private val _restTimePerExercise = MutableStateFlow(0)
    // MutableStateFlow for rest time between sets.
    private val _restTimePerSet = MutableStateFlow(0)
    // Publicly exposed StateFlows for observing sets, reps, and rest times.
    val sets: StateFlow<Int> = _sets
    val reps: StateFlow<Int> = _reps
    val restTimePerExercise: StateFlow<Int> = _restTimePerExercise
    val restTimePerSet: StateFlow<Int> = _restTimePerSet

    // Equipment selection
    // MutableStateFlow to hold a list of selected equipment (e.g., ["Dumbbells", "Barbell"]).
    private val _selectedEquipment = MutableStateFlow<List<String>>(emptyList())
    // Publicly exposed StateFlow for observing the selected equipment.
    val selectedEquipment: StateFlow<List<String>> = _selectedEquipment

    // Duration selection
    // MutableStateFlow for the desired workout duration in minutes, defaulting to 15 minutes.
    private val _duration = MutableStateFlow(15)
    // Publicly exposed StateFlow for observing the selected duration.
    val duration: StateFlow<Int> = _duration

    /**
     * Updates the [_selectedWorkout] with the chosen workout type.
     * Displays a Toast message to the user confirming the selection.
     * @param workout The name of the selected workout.
     * @param context The Android context, needed for displaying the Toast.
     */
    fun selectWorkout(workout: String, context: Context) {
        _selectedWorkout.value = workout
        Toast.makeText(context, "$workout selected", Toast.LENGTH_SHORT).show()
    }

    /**
     * Updates the [_selectedMuscleGroups] with the list of muscle groups chosen by the user.
     * @param muscleGroups A list of strings representing the selected muscle groups.
     */
    fun setMuscleGroups(muscleGroups: List<String>) {
        _selectedMuscleGroups.value = muscleGroups
    }

    /**
     * Updates the [_sets], [_reps], [_restTimePerExercise], and [_restTimePerSet] based on user input.
     * @param sets The number of sets per exercise.
     * @param reps The number of repetitions per set.
     * @param restTimePerExercise The rest time in minutes after each exercise.
     * @param restTimePerSet The rest time in minutes after each set.
     */
    fun setSetsAndReps(sets: Int, reps: Int, restTimePerExercise: Int, restTimePerSet: Int) {
        _sets.value = sets
        _reps.value = reps
        _restTimePerExercise.value = restTimePerExercise
        _restTimePerSet.value = restTimePerSet
    }

    /**
     * Updates the [_selectedEquipment] with the list of equipment chosen by the user.
     * @param equipment A list of strings representing the selected equipment.
     */
    fun setEquipment(equipment: List<String>) {
        _selectedEquipment.value = equipment
    }

    /**
     * Updates the [_duration] with the desired workout duration in minutes.
     * @param minutes The chosen workout duration in minutes.
     */
    fun setDuration(minutes: Int) {
        _duration.value = minutes
    }

    /**
     * Triggers the workout generation process in the [WorkoutViewModel].
     * Passes all selected criteria (muscle groups, sets, reps, equipment, duration) to it.
     * Displays a Toast message to the user and then navigates to the GeneratedWorkoutScreen.
     * @param nc The NavController used for navigation between screens.
     * @param context The Android context, needed for displaying the Toast.
     * @param workoutViewModel An instance of WorkoutViewModel to delegate the workout generation to.
     */
    fun generateWorkout(nc: NavController, context: Context, workoutViewModel: WorkoutViewModel) {
        // Call the generateWorkouts function from WorkoutViewModel with the current selections.
        workoutViewModel.generateWorkouts(
            muscles = selectedMuscleGroups.value,
            sets = sets.value,
            reps = reps.value,
            equipment = selectedEquipment.value,
            duration = duration.value
        )

        Toast.makeText(context, "Workout Generated!", Toast.LENGTH_SHORT).show()
        // Navigate to the GeneratedWorkoutScreen after generating the workout.
        nc.navigate(Screens.GeneratedWorkoutScreen.screen)
    }
}