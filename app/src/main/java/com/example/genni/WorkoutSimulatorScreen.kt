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
fun WorkoutSimulatorScreen(viewModel: WorkoutViewModel, onWorkoutCompleted: () -> Unit) {
    val workouts = viewModel.workouts
    val currentExerciseIndex by viewModel.currentExerciseIndex
    val currentSet by viewModel.currentSet
    val currentState by viewModel.currentState
    val timeLeft by viewModel.timeLeft
    val currentExercise = workouts.getOrNull(currentExerciseIndex)

    if (currentExercise == null) {
        onWorkoutCompleted()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(deepPurple, Color(0xFF111328))))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(currentExercise.imageResID),
            contentDescription = "Exercise Image",
            modifier = Modifier.size(150.dp)
        )

        Text(
            text = currentExercise.name,
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = white,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Set: $currentSet / ${currentExercise.sets}",
            fontSize = 22.sp,
            color = white
        )

        val timeSuffix = if (currentExercise.restTime == 1) "min" else "mins"
        Text(
            text = "Rest Time: ${currentExercise.restTime} $timeSuffix",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = white,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "${timeLeft}s",
            fontSize = 35.sp,
            color = white,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

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
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text("Skip Set", color = Color.White)
            }

            Button(
                onClick = { viewModel.skipExercise() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text("Skip Exercise", color = Color.White)
            }
        }

        Button(
            onClick = { viewModel.startWorkout(onWorkoutCompleted) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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

