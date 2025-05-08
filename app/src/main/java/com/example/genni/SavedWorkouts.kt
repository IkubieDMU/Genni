package com.example.genni

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.genni.models.SavedWorkout
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.softLavender
import com.example.genni.viewmodels.AuthViewModel
import com.example.genni.viewmodels.WorkoutViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SavedWorkoutsScreen(workoutViewModel: WorkoutViewModel, authViewModel: AuthViewModel, navController: NavController) {
    val savedWorkoutsList = workoutViewModel.savedWorkouts.collectAsState().value
    val context = LocalContext.current
    val user = authViewModel.currentUser.collectAsState().value

    LaunchedEffect(user?.username) {
        user?.username?.let { userId ->
            workoutViewModel.loadSavedWorkouts(
                userId = userId,
                onError = { err ->
                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, softLavender)))
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = "Saved Workouts",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )

        if (savedWorkoutsList.isEmpty()) {
            Text(
                text = "No saved workouts yet. Start generating and save some!",
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(savedWorkoutsList) { savedWorkout ->
                    AnimatedSavedWorkoutItem(
                        savedWorkout = savedWorkout,
                        onWorkoutClick = { selectedWorkout ->
                            workoutViewModel.loadWorkout(selectedWorkout)
                            navController.navigate(Screens.GeneratedWorkoutScreen.screen)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedSavedWorkoutItem(savedWorkout: SavedWorkout, onWorkoutClick: (SavedWorkout) -> Unit) {
    var isClicked by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isClicked) 0.95f else 1f, animationSpec = tween(durationMillis = 150))
    val alpha by animateFloatAsState(targetValue = if (isClicked) 0.8f else 1f, animationSpec = tween(durationMillis = 150))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .alpha(alpha)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                isClicked = true
                onWorkoutClick(savedWorkout)
            }
            .animateContentSize(animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = savedWorkout.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = deepPurple
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Saved on: ${formatTimestamp(savedWorkout.timestamp)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Filled.FitnessCenter,
                contentDescription = "Workout Icon",
                tint = emeraldGreen,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

// Helper function to format timestamp
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}