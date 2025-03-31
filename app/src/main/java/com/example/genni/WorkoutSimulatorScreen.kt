package com.example.genni

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.genni.models.Workout
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.white
import com.example.genni.viewmodels.WorkoutViewModel

@Composable
fun WorkoutSimulatorScreen(viewModel: WorkoutViewModel,onWorkoutCompleted: () -> Unit) {

    val workouts = viewModel.workouts // Retrieve the workouts from ViewModel
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
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(deepPurple, Color(0xFF111328)))).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painterResource(currentExercise.imageResID),"Content Description", modifier = Modifier.size(150.dp))
        Text(
            "${currentExercise.name}",
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = white,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            "Set: $currentSet / ${currentExercise.sets}",
            fontSize = 22.sp,
            color = white
        )

        // Minute Options
        var timeSuffix = ""

        if (currentExercise.restTime == 1) {
            timeSuffix = "min"
        } else {
            timeSuffix = "mins"
        }
        Text(
            "Rest Time: ${currentExercise.restTime} $timeSuffix", //60 * currentWorkout RestTime (in minutes) -> Convert to seconds
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = white,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            "${currentExercise.restTime * timeLeft}s", //60 * currentWorkout RestTime (in minutes) -> Convert to seconds
            fontSize = 35.sp,
            color = white,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Top Row Buttons (Skip Set and Skip Exercise)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = { viewModel.skipSet() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).padding(8.dp)
            ) {
                Text("Skip Set", color = Color.White)
            }

            Button(
                onClick = { viewModel.skipExercise() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).padding(8.dp)
            ) {
                Text("Skip Exercise", color = Color.White)
            }
        }

        // Start/Next Button Below
        Button(
            onClick = { viewModel.startWorkout(workouts, onWorkoutCompleted) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Start / Next", color = Color.White, modifier = Modifier.padding(8.dp))
        }
    }
}


// Preview of the Application
@Preview(showBackground = true)
@Composable
fun WorkoutSimulatorScreenPreview() {
    val viewModel = WorkoutViewModel()
    WorkoutSimulatorScreen(viewModel = viewModel) {}
}

