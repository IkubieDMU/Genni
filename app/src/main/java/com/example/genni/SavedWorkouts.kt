package com.example.genni

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.navigation.NavController
import com.example.genni.models.SavedWorkout
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.softLavender
import com.example.genni.viewmodels.AuthViewModel
import com.example.genni.viewmodels.WorkoutViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedWorkoutsScreen(viewModel: WorkoutViewModel, authViewModel: AuthViewModel, navController: NavController) {
    val savedWorkouts by viewModel.savedWorkouts.collectAsState()
    val context = LocalContext.current
    val user by authViewModel.currentUser.collectAsState()

    LaunchedEffect(user?.username) {
        user?.username?.let { userId ->
            viewModel.loadSavedWorkouts(
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
            .background(Brush.verticalGradient(listOf(softLavender, deepPurple, emeraldGreen)))
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = "Saved Workouts",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        if (savedWorkouts.isEmpty()) {
            Text(
                text = "No saved workouts yet!",
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
            )
        } else {
            LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                items(savedWorkouts) { item ->
                    SavedWorkoutItem(
                        savedWorkout = item,
                        onLoad = {
                            viewModel.loadWorkout(item)
                            navController.navigate(Screens.SavedWorkoutScreen.screen)
                            Toast.makeText(context, "Loaded ${item.name}", Toast.LENGTH_SHORT).show()
                        },
                        onDelete = {
                            user?.username?.let { currentUserId ->
                                viewModel.deleteSavedWorkout(
                                    workoutId = item.id,
                                    userId = currentUserId,
                                    onSuccess = {
                                        Toast.makeText(context, "Deleted ${item.name}", Toast.LENGTH_SHORT).show()
                                        viewModel.loadSavedWorkouts(userId = currentUserId)
                                    },
                                    onError = { error ->
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun SavedWorkoutItem(savedWorkout: SavedWorkout, onLoad: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = savedWorkout.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = deepPurple,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(savedWorkout.timestamp)),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Row {
                IconButton(onClick = onLoad) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Load Workout", tint = emeraldGreen)
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Workout", tint = Color.Red)
                }
            }
        }
    }
}