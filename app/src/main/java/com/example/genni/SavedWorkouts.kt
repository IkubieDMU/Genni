package com.example.genni

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.navigation.NavController
import com.example.genni.models.SavedWorkout
import com.example.genni.viewmodels.AuthViewModel
import com.example.genni.viewmodels.WorkoutViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedWorkoutsScreen(viewModel: WorkoutViewModel, authViewModel: AuthViewModel, navController: NavController) {
    val savedWorkouts = viewModel.savedWorkouts
    val context = LocalContext.current
    val user by authViewModel.currentUser.collectAsState()

    user?.let {
        viewModel.loadSavedWorkouts(
            userId = it.username,
            onError = { err ->
                Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
            }
        )
    }

    LazyColumn {
        items(savedWorkouts) { item ->
            SavedWorkoutItem(
                savedWorkout = item,
                onLoad = {
                    viewModel.loadWorkout(item)
                    navController.navigate(Screens.SavedWorkoutScreen.screen)
                    Toast.makeText(context, "Loaded ${item.name}", Toast.LENGTH_SHORT).show()
                },
                onDelete = {
                    user?.let { currentUser ->
                        viewModel.deleteSavedWorkout(
                            workoutId = item.id,
                            userId = currentUser.username,
                            onSuccess = {
                                Toast.makeText(context, "Deleted ${item.name}", Toast.LENGTH_SHORT).show()
                                viewModel.loadSavedWorkouts(userId = currentUser.username)
                            },
                            onError = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun SavedWorkoutItem(savedWorkout: SavedWorkout, onLoad: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = savedWorkout.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(savedWorkout.timestamp)),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row {
                IconButton(onClick = onLoad) { Icon(Icons.Default.PlayArrow, contentDescription = "Load Workout") }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete Workout") }
            }
        }
    }
}