package com.example.genni

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.genni.models.Workout
import com.example.genni.states.WorkoutState
import com.example.genni.ui.theme.GenniTheme
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.softLavender
import com.example.genni.viewmodels.WorkoutViewModel

@Composable
fun WorkoutSimulatorScreen(
    viewModel: WorkoutViewModel,
    workouts: List<Workout>,
    onWorkoutCompleted: () -> Unit
) {
    val currentExerciseIndex by viewModel.currentExerciseIndex
    val currentSet by viewModel.currentSet
    val currentState by viewModel.currentState
    val timeLeft by viewModel.timeLeft

    val currentExercise = workouts.getOrNull(currentExerciseIndex)

    if (currentExercise == null) {
        Text("Workout Completed!", fontSize = 24.sp, color = Color.White)
        onWorkoutCompleted()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, softLavender)))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Exercise: ${currentExercise.name}", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Set: $currentSet / ${currentExercise.sets}", fontSize = 20.sp, color = Color.White)
        Text("Time Left: ${timeLeft}s", fontSize = 20.sp, color = Color.White)

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { viewModel.skipSet() }) {
                Text("Skip Set")
            }
            Button(onClick = { viewModel.skipExercise() }) {
                Text("Skip Exercise")
            }
            Button(onClick = { viewModel.startWorkout(workouts, onWorkoutCompleted) }) {
                Text("Start / Next")
            }
        }
    }
}


/*@Preview(showBackground = true)
@Composable
fun WorkoutSimulatorScreenPreview() {
    // Sample Data for Preview
    val sampleWorkouts = listOf(
        Workout(index = 1, name = "Push-Ups", sets = 3, reps = 12, restTime = 1),
        Workout(index = 2, name = "Squats", sets = 3, reps = 15, restTime = 1),
        Workout(index = 3, name = "Plank", sets = 2, reps = 1, restTime = 2)
    )

    // Mock ViewModel for Preview
    val mockViewModel = WorkoutViewModel().apply {
        currentExerciseIndex = 0
        currentSet = 1
        currentState = WorkoutState.Exerise
        timeLeft = 30
    }

    WorkoutSimulatorScreen(viewModel = mockViewModel, workouts = sampleWorkouts) {
        // No-op for preview
    }
}*/

