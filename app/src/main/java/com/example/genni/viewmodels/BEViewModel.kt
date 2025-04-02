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

    // MutableStateFlow for breathing exercises
    private val _breathingExercises = MutableStateFlow<List<BreathingExercise>>(emptyList())
    val breathingExercises: StateFlow<List<BreathingExercise>> = _breathingExercises

    // Holds the index of the current breathing exercise
    private val _currentExerciseIndex = MutableStateFlow(0)
    val currentExerciseIndex: StateFlow<Int> = _currentExerciseIndex

    // Timer for each breathing phase (inhale, hold, exhale)
    private val _timeLeft = MutableStateFlow(0)
    val timeLeft: StateFlow<Int> = _timeLeft

    private var isRunning = false

    init {
        generateBreathingExercises()
    }

    /**
     * Generates a random list of breathing exercises for a 5-10 minute session
     */
    fun generateBreathingExercises() {
        val exercises = mutableListOf<BreathingExercise>()
        val totalDuration = (5..10).random() * 60 // Total time in seconds (5 to 10 minutes)
        var accumulatedTime = 0

        while (accumulatedTime < totalDuration) {
            val inhaleTime = (3..5).random()
            val holdTime = (2..4).random()
            val exhaleTime = (4..6).random()
            accumulatedTime += inhaleTime + holdTime + exhaleTime

            val exercise = BreathingExercise(inhaleTime, holdTime, exhaleTime)
            exercises.add(exercise)
        }
        _breathingExercises.value = exercises
    }

    /**
     * Starts or resumes the breathing session
     */
    fun startBreathingSession(onFinished: () -> Unit) {
        if (isRunning) return
        isRunning = true

        viewModelScope.launch {
            val currentExercise = _breathingExercises.value.getOrNull(_currentExerciseIndex.value)
            if (currentExercise != null) {
                startPhase(currentExercise.inhaleTime)
                startPhase(currentExercise.holdTime)
                startPhase(currentExercise.exhaleTime)

                _currentExerciseIndex.value++
                if (_currentExerciseIndex.value >= _breathingExercises.value.size) {
                    onFinished()
                } else {
                    isRunning = false
                }
            }
        }
    }

    private suspend fun startPhase(phaseTime: Int) {
        _timeLeft.value = phaseTime
        while (_timeLeft.value > 0) {
            delay(1000)
            _timeLeft.value -= 1
        }
    }

    /**
     * Skips the current exercise
     */
    fun skipExercise() {
        if (_currentExerciseIndex.value < _breathingExercises.value.size - 1) {
            _currentExerciseIndex.value++
        }
    }
}
