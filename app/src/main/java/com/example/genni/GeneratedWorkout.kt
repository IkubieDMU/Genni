package com.example.genni

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.genni.ui.theme.GenniTheme
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.royalPurple
import com.example.genni.ui.theme.white
import kotlin.random.Random

@Composable
fun GeneratedWorkoutScreen() {
    Column(modifier = Modifier.fillMaxSize().background(white).verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("Generated Workout", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = royalPurple, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(20.dp))

        Column {
            val exerciseNum = 4 // No. of workouts Generated...

            for (i in 1..exerciseNum) {
                val randomSetsNum = Random.nextInt(3,6) // 3-6 Sets
                val randomRepsNum = Random.nextInt(3,20) // 3-20 Reps
                val randomRestTimeNum = Random.nextInt(1,5) // 1-5 mins rest
                val randomWorkoutNum = Random.nextInt(0,24)
                // Array of Workouts (Sample)
                val workoutList = listOf(
                    "Push-Ups", "Bench Press", "Incline Dumbbell Press", "Chest Fly", "Dips",  // Chest
                    "Pull-Ups", "Deadlifts", "Bent-Over Rows", "Lat Pulldown", "Seated Cable Rows",  // Back
                    "Squats", "Lunges", "Leg Press", "Calf Raises", "Hamstring Curls",  // Legs
                    "Shoulder Press", "Lateral Raises", "Front Raises", "Shrugs", "Face Pulls",  // Shoulders
                    "Bicep Curls", "Triceps Dips", "Hammer Curls", "Overhead Triceps Extension", "Preacher Curls"  // Arms
                )

                Text("Exercise ${i} -> ${workoutList[randomWorkoutNum]}", color = royalPurple, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                WorkoutBlock(randomSetsNum.toString(), randomRepsNum.toString(), randomRestTimeNum.toString())
                Spacer(modifier = Modifier.height(20.dp))
            }
            Icon(Icons.Default.PlayArrow,null, tint = deepPurple, modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally))
        }

    }
}

@Composable
fun WorkoutBlock(sets:String,reps:String,restTime:String) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Row(
            modifier = Modifier.height(110.dp).width(360.dp).background(emeraldGreen, shape = RoundedCornerShape(15.dp)).padding(15.dp),
        ) {
            Icon(Icons.Default.Home,null, tint = white, modifier = Modifier.size(70.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Column{
                Text("$sets Sets", color = white, fontWeight = FontWeight.Bold)
                Text("$reps Reps", color = white, fontWeight = FontWeight.Bold)
                Text("Rest: ${restTime}min", color = white, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun GeneratedWorkoutScreenPreview() {
    GenniTheme { GeneratedWorkoutScreen() }
}*/
