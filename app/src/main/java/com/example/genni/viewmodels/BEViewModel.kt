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

    private val _breathingExercises = MutableStateFlow<List<BreathingExercise>>(emptyList())
    val breathingExercises: StateFlow<List<BreathingExercise>> = _breathingExercises

    private val _currentExerciseIndex = MutableStateFlow(0)
    val currentExerciseIndex: StateFlow<Int> = _currentExerciseIndex

    private val _timeLeft = MutableStateFlow(0)
    val timeLeft: StateFlow<Int> = _timeLeft

    private val _currentPhase = MutableStateFlow(BreathingPhase.INHALE)
    val currentPhase: StateFlow<BreathingPhase> = _currentPhase

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused

    private var isRunning = false

    enum class BreathingPhase { INHALE, HOLD, EXHALE }

    fun generateBreathingExercises(durationMinutes: Int = 5) {
        val exercises = mutableListOf<BreathingExercise>()
        val totalDuration = durationMinutes * 60
        var accumulatedTime = 0

        while (accumulatedTime < totalDuration) {
            val inhale = (3..5).random()
            val hold = (2..4).random()
            val exhale = (4..6).random()
            val total = inhale + hold + exhale

            if (accumulatedTime + total > totalDuration) break

            exercises.add(BreathingExercise(inhale, hold, exhale))
            accumulatedTime += total
        }

        _breathingExercises.value = exercises
        _currentExerciseIndex.value = 0
        _currentPhase.value = BreathingPhase.INHALE
        _timeLeft.value = 0
        isRunning = false
        _isPaused.value = false
    }

    fun startBreathingSession(onFinished: () -> Unit) {
        if (isRunning) return
        isRunning = true

        viewModelScope.launch {
            runCurrentExercise(onFinished)
        }
    }

    private suspend fun runCurrentExercise(onFinished: () -> Unit) {
        val current = breathingExercises.value.getOrNull(currentExerciseIndex.value)
        if (current == null) {
            onFinished()
            return
        }

        val phases = listOf(
            BreathingPhase.INHALE to current.inhaleTime,
            BreathingPhase.HOLD to current.holdTime,
            BreathingPhase.EXHALE to current.exhaleTime
        )

        for ((phase, time) in phases) {
            _currentPhase.value = phase
            _timeLeft.value = time

            while (_timeLeft.value > 0) {
                if (_isPaused.value) {
                    delay(200) // Wait while paused
                    continue
                }
                delay(1000)
                _timeLeft.value -= 1
            }
        }

        _currentExerciseIndex.value += 1
        isRunning = false

        if (_currentExerciseIndex.value >= breathingExercises.value.size) {
            onFinished()
        } else {
            startBreathingSession(onFinished)
        }
    }

    fun pause() {
        _isPaused.value = true
    }

    fun resume() {
        _isPaused.value = false
    }
}