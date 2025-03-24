package com.example.genni

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
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
import com.example.genni.ui.theme.softLavender
import com.example.genni.ui.theme.white
import kotlin.random.Random

@Composable
fun GeneratedWorkoutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, softLavender)))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            "Generated Workout",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = white,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        val exerciseNum = 10 // Number of workouts generated
        val workoutList = listOf(
            "Push-Ups", "Bench Press", "Incline Dumbbell Press", "Chest Fly", "Dips",
            "Pull-Ups", "Deadlifts", "Bent-Over Rows", "Lat Pulldown", "Seated Cable Rows",
            "Squats", "Lunges", "Leg Press", "Calf Raises", "Hamstring Curls",
            "Shoulder Press", "Lateral Raises", "Front Raises", "Shrugs", "Face Pulls",
            "Bicep Curls", "Triceps Dips", "Hammer Curls", "Overhead Triceps Extension", "Preacher Curls"
        )

        for (i in 1..exerciseNum) {
            val randomSetsNum = Random.nextInt(3, 6) // 3-6 Sets
            val randomRepsNum = Random.nextInt(3, 20) // 3-20 Reps
            val randomRestTimeNum = Random.nextInt(1, 5) // 1-5 mins rest
            val randomWorkout = workoutList[Random.nextInt(workoutList.size)]

            WorkoutCard(i, randomWorkout, randomSetsNum, randomRepsNum, randomRestTimeNum)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun WorkoutCard(index: Int, workoutName: String, sets: Int, reps: Int, restTime: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(emeraldGreen, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                /*Image(
                    painter = painterResource(id = R.drawable.gym_logo),
                    contentDescription = "Workout Icon",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Crop
                )*/
            }
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Exercise $index: $workoutName",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = deepPurple
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Sets: $sets | Reps: $reps | Rest: $restTime min", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GeneratedWorkoutScreenPreview() {
    GenniTheme { GeneratedWorkoutScreen() }
}
