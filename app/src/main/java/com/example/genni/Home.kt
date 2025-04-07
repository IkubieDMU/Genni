package com.example.genni

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.res.painterResource
import com.example.genni.ui.theme.white
import com.example.genni.viewmodels.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(nc: NavController, viewModel: HomeViewModel, authViewModel: AuthViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentUser by authViewModel.currentUser.collectAsState() // Current User's data

    // List of drawer item names and their respective routes
    val drawerItems = listOf(
        "Home" to Screens.HomeScreen.screen,
        "Health Calculations" to Screens.HealthCalculationsScreen.screen,
        "Breathing Exercises" to Screens.BreathingExercisesScreen.screen,
        "Settings" to Screens.SettingsScreen.screen,
        "About" to Screens.AboutScreen.screen
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(drawerItems, nc, drawerState, scope,authViewModel) }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Genni", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        //When Clicked, open the Nav Drawer
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = emeraldGreen)
                )
            },
            /*bottomBar = { ModernBottomAppBar() } -> Bottom App Bar Code*/
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

                    Image(painterResource(R.drawable.genniappiconnb), contentDescription = "Genni's Logo", modifier = Modifier.size(70.dp))
                    Text("Welcome Back ${currentUser?.firstName ?: "User"}!", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Your AI-Powered Workout Partner", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(40.dp))

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
                                        selectedWorkout = workoutTitles[index]
                                    }
                                }
                            }
                        }
                    }

                    if (selectedWorkout != null) {
                        AlertDialog(
                            onDismissRequest = { selectedWorkout = null },
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

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(emeraldGreen.copy(alpha = 0.9f))
                            .clickable { viewModel.generateWorkout(nc, context) }
                            .border(2.dp, Color.White.copy(alpha = 0.7f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(50.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerContent(drawerItems: List<Pair<String, String>>, nc: NavController, drawerState: DrawerState, scope: CoroutineScope, authViewModel: AuthViewModel) {
    // Smooth gradient background with a floating effect
    Box(
        modifier = Modifier.fillMaxHeight().width(250.dp)
            .background(Brush.verticalGradient(listOf(deepPurple, emeraldGreen)))
            .shadow(10.dp, RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            val currentUser by authViewModel.currentUser.collectAsState()
            // Header Section with Profile Picture and Welcome Message
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // User Avatar (Placeholder)
                    Box(
                        modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)).border(2.dp, Color.White, CircleShape)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White, modifier = Modifier.size(50.dp).align(Alignment.Center))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Welcome ${currentUser?.firstName ?: "User"}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Drawer Items
            drawerItems.forEachIndexed { index, (title, route) ->
                TextButton(
                    onClick = {
                        scope.launch { drawerState.close() }
                        nc.navigate(route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(
                            if (index % 2 == 0) Color.White.copy(alpha = 0.1f)
                            else Color.White.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = when (title) {
                                "Home" -> Icons.Default.Home
                                "Health Calculations" -> Icons.Default.FitnessCenter
                                "Breathing Exercises" -> Icons.Default.Air
                                "Settings" -> Icons.Default.Settings
                                "About" -> Icons.Default.Info
                                else -> Icons.Default.Menu
                            },
                            contentDescription = title,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(title, color = Color.White, fontSize = 16.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(100.dp))

            // Footer Section (Version Info)
            Text(
                text = "Version 8.8.0",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ModernBottomAppBar() {
    Box(
        modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp).height(70.dp).clip(RoundedCornerShape(20.dp)).background(Brush.horizontalGradient(listOf(emeraldGreen.copy(alpha = 0.8f), deepPurple.copy(alpha = 0.8f))))
    ) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
            IconButton(onClick = { /* Home Click */ }) {
                Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
            }
            IconButton(onClick = { /* Search Click */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
            IconButton(onClick = { /* Profile Click */ }) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
            }
            IconButton(onClick = { /* Settings Click */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
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
    ) {
        Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val nc = rememberNavController()
    GenniTheme { HomeScreen(nc, HomeViewModel(), AuthViewModel()) }
}