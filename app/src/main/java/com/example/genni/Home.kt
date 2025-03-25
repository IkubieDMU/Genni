package com.example.genni

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.genni.ui.theme.GenniTheme
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.softLavender
import com.example.genni.viewmodels.HomeViewModel
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(nc: NavController, viewModel: HomeViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout App", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* Handle menu click */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = emeraldGreen)
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, softLavender))),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val context = LocalContext.current
                var selectedWorkout by remember { mutableStateOf<String?>(null) }

                // Welcome Text
                Text("Welcome Back!", fontSize = 35.sp, fontWeight = FontWeight.Bold, color = Color.White)

                Text("Your AI-Powered Workout Partner", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)

                Spacer(modifier = Modifier.height(40.dp))

                // 2x2 Workout Grid
                val workoutTitles = listOf("Muscle Groups", "Sets & Reps", "Equipment", "Duration")

                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    for (i in 0..1) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (j in 0..1) {
                                val index = i * 2 + j
                                WorkoutBox(workoutTitles[index]) {
                                    selectedWorkout = workoutTitles[index] // Set selected workout for popup
                                }
                            }
                        }
                    }
                }

                // Show Popup when a workout is clicked
                if (selectedWorkout != null) {
                    AlertDialog(
                        onDismissRequest = { selectedWorkout = null }, // Close popup when dismissed
                        confirmButton = {
                            TextButton(onClick = { selectedWorkout = null }) {
                                Text("OK", color = Color.White)
                            }
                        },
                        title = { Text("Workout Info", color = Color.White) },
                        text = { Text("You selected: $selectedWorkout", color = Color.White.copy(alpha = 0.8f)) }
                    )

                }

                Spacer(modifier = Modifier.height(50.dp))

                // Animated Generate Workout Button
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(emeraldGreen.copy(alpha = 0.9f))
                        .clickable { viewModel.generateWorkout(nc, context) }
                        .shadow(8.dp, CircleShape, clip = false)
                        .border(2.dp, Color.White.copy(alpha = 0.7f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(50.dp))
                }
            }
        }
    }
}

@Composable
fun WorkoutBox(title: String, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.15f), Color.White.copy(alpha = 0.05f))))
            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .shadow(6.dp, RoundedCornerShape(20.dp), clip = false)
    ) {
        Text(
            title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
    }
}





@Preview
@Composable
fun HomeScreenPreview() {
    val nc = rememberNavController()
    GenniTheme { HomeScreen(nc, HomeViewModel()) }
}