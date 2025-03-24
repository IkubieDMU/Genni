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

    // Holds the selected workout category
    // MutableStateFlow allows us to update and observe state changes reactively
    private val _selectedWorkout = MutableStateFlow<String?>(null)

    // Publicly exposed immutable StateFlow so the UI can observe changes
    val selectedWorkout: StateFlow<String?> = _selectedWorkout

    /**
     * Updates the selected workout category when a user clicks on a workout option.
     * @param workout The name of the selected workout category.
     */
    fun selectWorkout(workout: String,context: Context) {
        _selectedWorkout.value = workout  // Updates the selected workout state
        Toast.makeText(context, "$workout selected", Toast.LENGTH_SHORT).show()
    }

    /**
     * Triggers workout generation, displays a Toast message, and navigates to the GeneratedWorkoutScreen.
     * @param nc The NavController used for navigation.
     * @param context The application context required for showing the Toast message.
     */
    fun generateWorkout(nc: NavController, context: Context) {
        // Show a message to confirm workout generation
        Toast.makeText(context, "Workout Generated!", Toast.LENGTH_SHORT).show()
        // Navigate to the generated workout screen
        nc.navigate(Screens.GeneratedWorkoutScreen.screen)
    }
}
