package com.example.genni

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.genni.models.BreathingExercise
import com.example.genni.ui.theme.GenniTheme
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.softLavender
import com.example.genni.ui.theme.white
import com.example.genni.viewmodels.BEViewModel

@Composable
fun BreathingExercisesScreen(navController: NavController, beViewModel: BEViewModel) {
    var selectedDuration by remember { mutableStateOf(5) }

    // Regenerate session whenever duration changes
    LaunchedEffect(selectedDuration) {
        beViewModel.generateBreathingExercises(durationMinutes = selectedDuration)
    }

    val breathingExercises = beViewModel.breathingExercises.collectAsState().value
    val context = LocalContext.current.applicationContext

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, softLavender)))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            "Generated Breathing Exercise Session",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Duration Selector
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Select Duration: ", fontSize = 18.sp, color = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            DropdownMenuDurationSelector(
                selectedDuration = selectedDuration,
                onDurationSelected = { selectedDuration = it }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (breathingExercises.isEmpty()) {
            Text(
                "No exercises generated.",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            breathingExercises.forEachIndexed { index, exercise ->
                BreathingExerciseCard(exercise, index + 1)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                StartButton(context = context, navController = navController)
            }
        }
    }
}

@Composable
fun DropdownMenuDurationSelector(selectedDuration: Int, onDurationSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = (5..10).toList()

    Box {
        Button(onClick = { expanded = true }) {
            Text("$selectedDuration min")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { duration ->
                DropdownMenuItem(
                    text = { Text("$duration minutes") },
                    onClick = {
                        onDurationSelected(duration)
                        expanded = false
                    }
                )
            }
        }
    }
}



@Composable
fun BreathingExerciseCard(exercise: BreathingExercise, exerciseNumber: Int) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).shadow(8.dp, RoundedCornerShape(20.dp)).clip(RoundedCornerShape(20.dp)),
        backgroundColor = white
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(color = white, shape = CircleShape)
                    .clip(CircleShape)
                    .border(width = 2.dp, color = deepPurple, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder image for breathing exercise
                Icon(Icons.Default.Air, contentDescription = null, modifier = Modifier.size(50.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text("Exercise ${exerciseNumber}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    text = "Inhale - ${exercise.inhaleTime}s |  Hold - ${exercise.holdTime}s  |  Exhale - ${exercise.exhaleTime}s",
                    fontSize = 16.sp,
                    color = deepPurple
                )
            }
        }
    }
}

@Composable
fun StartButton(context: Context, navController: NavController) {
    Box(
        modifier = Modifier.padding(vertical = 20.dp).fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = {
                navController.navigate(Screens.BreathingExercisesSimulatorScreen.screen)
            },
            modifier = Modifier
                .size(80.dp)
                .shadow(10.dp, CircleShape)
                .background(Brush.radialGradient(listOf(deepPurple, emeraldGreen)), CircleShape)
                .border(2.dp, Color.White, CircleShape)
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start Breathing Session", tint = Color.White, modifier = Modifier.size(40.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BEPreview() {
    val nc = rememberNavController()
    GenniTheme { BreathingExercisesScreen(nc,BEViewModel()) }
}
