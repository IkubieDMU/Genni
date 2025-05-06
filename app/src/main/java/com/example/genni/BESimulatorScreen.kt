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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val currentPhase by viewModel.currentPhase.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()

    val currentExercise = breathingExercises.getOrNull(currentExerciseIndex)
    var started by remember { mutableStateOf(false) }

    if (currentExercise == null) {
        onBreathingSessionCompleted()
        return
    }

    if (breathingExercises.isEmpty()) {
        CircularProgressIndicator(color = Color.White)
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
        Text(
            text = "Breathing Exercise ${currentExerciseIndex + 1}",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = currentPhase.name.lowercase().replaceFirstChar { it.uppercase() },
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
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = {
                    if (isPaused) viewModel.resume()
                    else viewModel.pause()
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (isPaused) Color(0xFF4CAF50) else Color(0xFFFFA000)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).padding(8.dp)
            ) {
                Text(if (isPaused) "Resume" else "Pause", color = Color.White)
            }
        }

        Button(
            onClick = {
                if (!started) {
                    started = true
                    viewModel.startBreathingSession(onBreathingSessionCompleted)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Start", color = Color.White, modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BESPreview() { GenniTheme { BESimulatorScreen(BEViewModel()) { } } }

