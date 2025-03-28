package com.example.genni

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun GeneratedWorkoutScreen(viewModel: WorkoutViewModel, navController: NavController) {
    val workouts by remember { derivedStateOf { viewModel.workouts }}
    val context = LocalContext.current.applicationContext

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

        workouts.forEach { workout ->
            WorkoutCard(workout)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Column(modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)) {
            PlayButton(context = context, navController = navController, workouts = workouts)
        }
    }
}


@Composable
fun WorkoutCard(workout: Workout) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(80.dp).background(emeraldGreen, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                /*Image( //TODO: Do something with this later
                    painter = painterResource(id = R.drawable.gym_logo),
                    contentDescription = "Workout Icon",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Crop
                )*/
            }
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Exercise ${workout.index}: ${workout.name}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = deepPurple
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Sets: ${workout.sets} | Reps: ${workout.reps} | Rest: ${workout.restTime} min(s)", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun PlayButton(context: Context, navController: NavController, workouts: List<Workout>) {
    IconButton(
        onClick = {
            if (workouts.isNotEmpty()) {
                navController.navigate(Screens.WorkoutSimulatorScreen.screen) //Redirect to the Workout Simulator Screen....
            } else {
                Toast.makeText(context, "No workouts to generate!", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier.size(60.dp).shadow(8.dp, CircleShape).background(deepPurple, CircleShape)
    ) {
        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start Workout", tint = Color.White, modifier = Modifier.size(36.dp))
    }
}




@Preview(showBackground = true)
@Composable
fun GeneratedWorkoutScreenPreview() {
    val nc = rememberNavController()
    GenniTheme { GeneratedWorkoutScreen(WorkoutViewModel(),nc) }
}
