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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.genni.ui.theme.GenniTheme
import com.example.genni.ui.theme.deepPurple
import com.example.genni.viewmodels.BEViewModel

@Composable
fun BESimulatorScreen(viewModel: BEViewModel, onBreathingSessionCompleted: () -> Unit) {
    val breathingExercises by viewModel.breathingExercises.collectAsState()
    val currentExerciseIndex by viewModel.currentExerciseIndex.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()

    val currentExercise = breathingExercises.getOrNull(currentExerciseIndex)

    if (currentExercise == null) {
        Text(
            "Breathing Session Completed!",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            textAlign = TextAlign.Center
        )
        onBreathingSessionCompleted()
        return
    }

    LaunchedEffect(currentExerciseIndex, timeLeft) {
        if (timeLeft == 0) {
            viewModel.startBreathingSession(onBreathingSessionCompleted)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(deepPurple, Color(0xFF111328))))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Breathing Exercise ${currentExerciseIndex + 1}",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = when {
                timeLeft > currentExercise.holdTime + currentExercise.exhaleTime -> "Inhale"
                timeLeft > currentExercise.exhaleTime -> "Hold"
                else -> "Exhale"
            },
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "$timeLeft s",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
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
                onClick = { viewModel.skipExercise() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).padding(8.dp)
            ) {
                Text("Skip Exercise", color = Color.White)
            }
        }

        Button(
            onClick = { viewModel.startBreathingSession(onBreathingSessionCompleted) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Start / Next", color = Color.White, modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BESPreview() { GenniTheme { BESimulatorScreen(BEViewModel()) { } } }

