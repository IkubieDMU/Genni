package com.example.genni.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genni.models.BreathingExercise
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class BEViewModel : ViewModel() {

    // A MutableStateFlow holding a list of BreathingExercise objects.
    // This list represents the sequence of breathing exercises generated for a session.
    private val _breathingExercises = MutableStateFlow<List<BreathingExercise>>(emptyList())
    // Publicly exposed StateFlow to allow the UI to observe changes to the list of breathing exercises.
    val breathingExercises: StateFlow<List<BreathingExercise>> = _breathingExercises

    // A MutableStateFlow tracking the index of the currently active breathing exercise in the list.
    private val _currentExerciseIndex = MutableStateFlow(0)
    // Publicly exposed StateFlow for observing the current exercise index.
    val currentExerciseIndex: StateFlow<Int> = _currentExerciseIndex

    // A MutableStateFlow tracking the remaining time for the current breathing phase.
    private val _timeLeft = MutableStateFlow(0)
    // Publicly exposed StateFlow for observing the time left.
    val timeLeft: StateFlow<Int> = _timeLeft

    // A MutableStateFlow indicating the current phase of the breathing exercise (INHALE, HOLD, EXHALE).
    private val _currentPhase = MutableStateFlow(BreathingPhase.INHALE)
    // Publicly exposed StateFlow for observing the current breathing phase.
    val currentPhase: StateFlow<BreathingPhase> = _currentPhase

    // A MutableStateFlow indicating whether the breathing session is currently paused.
    private val _isPaused = MutableStateFlow(false)
    // Publicly exposed StateFlow for observing the paused state.
    val isPaused: StateFlow<Boolean> = _isPaused

    // A private boolean flag to track if a breathing session is actively running to prevent multiple concurrent sessions.
    private var isRunning = false

    // An enum class defining the different phases of a breathing exercise.
    enum class BreathingPhase { INHALE, HOLD, EXHALE }

    /**
     * Generates a sequence of random breathing exercises to fill a specified duration.
     * Each exercise consists of inhale, hold, and exhale times.
     * The method ensures the total accumulated time of exercises does not exceed the `durationMinutes`.
     *
     * @param durationMinutes The desired total duration of the breathing session in minutes. Defaults to 5 minutes.
     */
    fun generateBreathingExercises(durationMinutes: Int = 5) {
        val exercises = mutableListOf<BreathingExercise>()
        val totalDuration = durationMinutes * 60 // Convert duration to seconds.
        var accumulatedTime = 0 // Tracks the total time of exercises added so far.

        // Loop to generate exercises until the accumulated time meets or exceeds the total duration.
        while (accumulatedTime < totalDuration) {
            // Generate random times for each phase.
            val inhale = (3..5).random() // Inhale time between 3 and 5 seconds.
            val hold = (2..4).random() // Hold time between 2 and 4 seconds.
            val exhale = (4..6).random() // Exhale time between 4 and 6 seconds.
            val total = inhale + hold + exhale // Total time for this single exercise.

            // If adding this exercise would exceed the total duration, break the loop.
            if (accumulatedTime + total > totalDuration) break

            // Add the new breathing exercise to the list.
            exercises.add(BreathingExercise(inhale, hold, exhale))
            accumulatedTime += total // Update accumulated time.
        }

        // Update the StateFlows with the generated exercises and reset session state.
        _breathingExercises.value = exercises
        _currentExerciseIndex.value = 0
        _currentPhase.value = BreathingPhase.INHALE
        _timeLeft.value = 0
        isRunning = false
        _isPaused.value = false
    }

    /**
     * Starts the breathing session.
     * It checks if a session is already running to prevent re-triggering.
     * Launches a coroutine to run the first exercise.
     *
     * @param onFinished A callback function to be invoked when the entire breathing session is completed.
     */
    fun startBreathingSession(onFinished: () -> Unit) {
        // If a session is already running, do nothing.
        if (isRunning) return
        isRunning = true // Set running flag to true.

        // Launch a coroutine in the ViewModel's scope to manage the session.
        viewModelScope.launch {
            runCurrentExercise(onFinished) // Start running the exercises.
        }
    }

    /**
     * Internal suspend function that manages the timing and phase transitions for the current breathing exercise.
     * It progresses through inhale, hold, and exhale phases, updating time left and current phase.
     * Recursively calls `startBreathingSession` to move to the next exercise or `onFinished` if all exercises are done.
     *
     * @param onFinished A callback function to be invoked when the entire breathing session is completed.
     */
    private suspend fun runCurrentExercise(onFinished: () -> Unit) {
        // Get the current breathing exercise based on the index.
        val current = breathingExercises.value.getOrNull(currentExerciseIndex.value)
        // If there's no current exercise (e.g., list is empty or index is out of bounds), session is finished.
        if (current == null) {
            onFinished()
            return
        }

        // Define the sequence of phases for the current exercise with their respective durations.
        val phases = listOf(
            BreathingPhase.INHALE to current.inhaleTime,
            BreathingPhase.HOLD to current.holdTime,
            BreathingPhase.EXHALE to current.exhaleTime
        )

        // Iterate through each phase of the current exercise.
        for ((phase, time) in phases) {
            _currentPhase.value = phase // Update the current phase.
            _timeLeft.value = time // Set the time left for this phase.

            // Countdown loop for the current phase.
            while (_timeLeft.value > 0) {
                // If the session is paused, delay and continue the loop without decrementing time.
                if (_isPaused.value) {
                    delay(200) // Small delay to avoid busy-waiting.
                    continue // Skip to the next iteration to check pause state again.
                }
                delay(1000) // Wait for 1 second.
                _timeLeft.value -= 1 // Decrement time.
            }
        }

        _currentExerciseIndex.value += 1 // Move to the next exercise.
        isRunning = false // Reset running flag as this exercise is complete.

        // Check if all exercises in the list have been completed.
        if (_currentExerciseIndex.value >= breathingExercises.value.size) {
            onFinished() // If yes, call the onFinished callback.
        } else {
            // If there are more exercises, restart the session to run the next one.
            startBreathingSession(onFinished)
        }
    }

    /**
     * Pauses the current breathing session by setting the `_isPaused` flag to true.
     */
    fun pause() {
        _isPaused.value = true
    }

    /**
     * Resumes the current breathing session by setting the `_isPaused` flag to false.
     */
    fun resume() {
        _isPaused.value = false
    }
}