package com.example.genni

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.genni.ui.theme.darkGray
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.mintGreen
import com.example.genni.ui.theme.royalPurple
import com.example.genni.ui.theme.softLavender
import com.example.genni.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(nc: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout App", color = deepPurple, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* Handle menu click */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = deepPurple)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = white)
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = royalPurple, contentColor = white) {
                IconButton(onClick = { /* Handle home click */ }) {
                    Icon(Icons.Default.Home, contentDescription = "Home")
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* Handle workout click */ }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Workout")
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* Handle settings click */ }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(white),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                val context = LocalContext.current.applicationContext
                Spacer(modifier = Modifier.height(8.dp))
                // Welcome Text
                Text(
                    "Welcome User",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = deepPurple
                )

                Text(
                    "I'm simple, You choose and I create",
                    color = darkGray,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(50.dp))

                // 2X2 Workout Grid
                val workoutTitles = listOf("Muscle Groups", "Sets & Reps", "Equipment", "Duration")
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    for (i in 0..1) {
                        Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.fillMaxWidth()) {
                            for (j in 0..1) {
                                val index = i * 2 + j //Index Formula for Grids -> Outer loop index(i) * No. of Columns in Grid(2) + Inner Loop Index(j)
                                WorkoutBox(workoutTitles[index]) {
                                    Toast.makeText(context, "${workoutTitles[index]} selected!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Generate Workout Button
                IconButton(
                    onClick = {
                        Toast.makeText(context, "Workout Generated!", Toast.LENGTH_SHORT).show()
                        nc.navigate(Screens.GeneratedWorkoutScreen.screen) //Redirect to the Generated Workout Screen
                              },
                    modifier = Modifier.border(1.dp, deepPurple, shape = CircleShape).size(80.dp)
                ) {
                    Icon(Icons.Default.PlayArrow,null, tint = deepPurple, modifier = Modifier.size(50.dp))
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
            .size(150.dp)
            .background(
                color = white,
                shape = RoundedCornerShape(16.dp)
            )
            .border(1.dp, royalPurple, shape = RoundedCornerShape(16.dp)) // Green outline
            .clickable { onClick() }
    ) {
        Text(
            title,
            color = deepPurple,
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
    GenniTheme { HomeScreen(nc) }
}