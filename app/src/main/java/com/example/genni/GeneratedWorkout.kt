package com.example.genni

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.genni.models.Workout
import com.example.genni.ui.theme.GenniTheme
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.royalPurple
import com.example.genni.ui.theme.softLavender
import com.example.genni.ui.theme.white
import com.example.genni.viewmodels.WorkoutViewModel
import kotlin.random.Random

@Composable
fun GeneratedWorkoutScreen(workoutViewModel: WorkoutViewModel, navController: NavController) {
    val workouts by remember { derivedStateOf { workoutViewModel.workouts } }
    val context = LocalContext.current.applicationContext

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, softLavender)))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = "Generated Workout",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        workouts.forEachIndexed { index, workout ->
            WorkoutCard(workout = workout.copy(index = index + 1))
            Spacer(modifier = Modifier.height(16.dp))
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            PlayButton(
                context = context,
                navController = navController,
                workouts = workouts
            )
        }
    }
}




@Composable
fun WorkoutCard(workout: Workout) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple)), shape = CircleShape)
                        .clip(CircleShape)
                        .border(2.dp, deepPurple, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = workout.imageResID),
                        contentDescription = "Workout Icon",
                        modifier = Modifier.size(50.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Exercise ${workout.index}: ${workout.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = deepPurple
                    )
                    Text(
                        text = "Sets: ${workout.sets} | Reps: ${workout.reps} | Rest: ${workout.restTime} min",
                        fontSize = 14.sp,
                        color = deepPurple.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Muscles: ${workout.muscleGroupWorked.joinToString()}",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f)
            )
            Text(
                text = "Equipment: ${workout.equipmentUsed.joinToString()}",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f)
            )
        }
    }
}


@Composable
fun PlayButton(context: Context, navController: NavController, workouts: List<Workout>) {
    Box(
        modifier = Modifier
            .padding(vertical = 20.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = {
                if (workouts.isNotEmpty()) {
                    navController.navigate(Screens.WorkoutSimulatorScreen.screen)
                } else {
                    Toast.makeText(context, "No workouts to generate!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .size(80.dp)
                .shadow(10.dp, CircleShape)
                .background(Brush.radialGradient(listOf(deepPurple, emeraldGreen)), CircleShape)
                .border(2.dp, Color.White, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Start Workout",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GeneratedWorkoutScreenPreview() {
    val nc = rememberNavController()
    GenniTheme { GeneratedWorkoutScreen(WorkoutViewModel(),nc) }
}
