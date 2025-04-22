package com.example.genni

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.navigation.NavController
import com.example.genni.viewmodels.WorkoutViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedWorkoutsScreen(navController: NavController, workoutViewModel: WorkoutViewModel) {
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    val savedWorkouts = workoutViewModel.savedWorkouts

    LaunchedEffect(userId) {
        userId?.let { workoutViewModel.loadSavedWorkouts(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Workouts") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (savedWorkouts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No saved workouts found.")
            }
        } else {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {

                items(savedWorkouts.toList()) { pair ->
                    val (name, exercises) = pair
                    SavedWorkoutItem(
                        name = name,
                        onLoadClicked = {
                            workoutViewModel.setCurrentWorkout(exercises)
                            navController.navigate(Screens.WorkoutSimulatorScreen.screen)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SavedWorkoutItem(name: String, onLoadClicked: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onLoadClicked, modifier = Modifier.align(Alignment.End)) {
                Text("Load Workout")
            }
        }
    }
}
